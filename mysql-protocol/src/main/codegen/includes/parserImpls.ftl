<#--
// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to you under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
-->

<#--
  Add implementations of additional parser statements, literals or
  data types.

  Example of SqlShowTables() implementation:
  SqlNode SqlShowTables()
  {
    ...local variables...
  }
  {
    <SHOW> <TABLES>
    ...
    {
      return SqlShowTables(...)
    }
  }
-->

SqlCreateDb CreateDatabase() :
{
    final SqlIdentifier dbName;
    final SqlNodeList columnList;
    boolean isNotExist = false;
    SqlParserPos pos;
}
{
    {
        pos = getPos();
    }
    <CREATE> <DATABASE>
    (
        <IF> <NOT> <EXISTS>
        {
            isNotExist = true;
        }
    )?

    dbName = CompoundIdentifier()
    {
        return new SqlCreateDb(pos, false, isNotExist, dbName.toString());
    }
}

SqlDrop Drop() :
{
    final SqlIdentifier dbName;
    final SqlNodeList columnList;
    boolean exist = false;
    boolean isDb = true;
    SqlParserPos pos;
}
{
    {
        pos = getPos();
    }
    <DROP>
    (
        <DATABASE>
        |
        <TABLE>
        {
            isDb = false;
        }
    )

    (
        <IF> <EXISTS>
        {
            exist = true;
        }
    )?

    dbName = CompoundIdentifier()
    {
        return new SqlDrop(pos, exist, dbName.toString(), isDb);
    }
}


SqlShow SqlShow() :
{
    SqlIdentifier command = null;
    SqlParserPos pos;
    int type;
}
{
    {
        pos = getPos();
    }

    <SHOW>
    (
    <DATABASES>
    {
        type = 0;
    }
    |
    <TABLES>
    {
        type = 1;
    }
    |
    <CREATE>
    <TABLE>
    command = CompoundIdentifier()
    {
        type = 2;
    }
    )
    {
    return new SqlShow(pos, type, command == null ? null : command.toString());
    }
}

SqlUse SqlUseCommand() :
{
    final SqlIdentifier command;
    SqlParserPos pos;
}
{
    {
        pos = getPos();
    }
    <USE>
    command = CompoundIdentifier()
    {
        return new SqlUse(pos, command.toString());
    }
}


SqlCreateTable CreateTable() :
{
    final SqlIdentifier schemaAndTableName;
    boolean isNotExist = false;
    SqlParserPos pos;
    SqlNodeList sqlNodeList;

    String engine = null;
    SqlNode tableComment = null;
}
{
    {
        pos = getPos();
    }

    <CREATE> <TABLE>
    (
        <IF> <NOT> <EXISTS>
            {
                isNotExist = true;
            }
    )?
    schemaAndTableName = CompoundIdentifier()
    sqlNodeList = SlothColumnTypes()
    [
        <ENGINE> <EQ>
        engine = Identifier()
    ]
    [
        <COMMENT> <EQ>
        tableComment =  Literal()
    ]
    {
        return new SqlCreateTable(pos, schemaAndTableName.toString(), sqlNodeList, isNotExist, engine, tableComment);
    }
}


SqlNodeList SlothColumnTypes() :
{
    final Span s;
    List<SqlNode> list = new ArrayList<SqlNode>();
}
{
        <LPAREN> { s = span(); }
        SlothColumnType(list)
        (
            <COMMA> SlothColumnType(list)
        )*
        <RPAREN> {
            return new SqlNodeList(list, s.end(this));
        }
}

void SlothColumnType(List<SqlNode> list) :
{
    SqlIdentifier name;
    SqlDataTypeSpec type;
    boolean nullable = true;
    boolean unsigned = false;
    SqlNode defalutValue = null;
    SqlNode comment = null;
    SqlNode precision = null;
}
{
    name = CompoundIdentifier()
    type = DataType()
    [
        <LPAREN>
            precision = Literal()
        <RPAREN>
    ]

    [
       <UNSIGNED>  {
           unsigned = true;
       }
    ]

    [
        <NOT> <NULL> {
            nullable = false;
        }
    ]

    [
        <DEFAULT_>
        defalutValue = Literal()
    ]

    [
        <COMMENT>
        comment = Literal()
    ]

    {
        list.add(name);
        type = type.withNullable(nullable);
        list.add(new SlothColumnType(
            type,
            precision,
            unsigned,
            defalutValue,
            comment
        ));
    }
}

SqlTypeNameSpec SlothSpecailType(Span s) :
{
    SqlTypeName sqlTypeName = null;
}
{
    (
        (
        <TEXT>
        |
        <TINYTEXT>
        |
        <MEDIUMTEXT>
        |
        <LONGTEXT>
        )
        {
            sqlTypeName = SqlTypeName.VARCHAR;
        }
    )
    {
        return new SqlBasicTypeNameSpec(sqlTypeName, s.end(this));
    }
}

