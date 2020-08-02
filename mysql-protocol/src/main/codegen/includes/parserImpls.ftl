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

SqlDropDb DropDatabase() :
{
    final SqlIdentifier dbName;
    final SqlNodeList columnList;
    boolean exist = false;
    SqlParserPos pos;
}
{
    {
        pos = getPos();
    }
    <DROP> <DATABASE>
    (
        <IF> <EXISTS>
        {
            exist = true;
        }
    )?

    dbName = CompoundIdentifier()
    {
        return new SqlDropDb(pos, exist, dbName.toString());
    }
}

SqlShow SqlShow() :
{
    final SqlIdentifier command;
    SqlParserPos pos;
}
{
    {
        pos = getPos();
    }

    <SHOW>
    command  =  CompoundIdentifier()
    {
        return new SqlShow(pos, command.toString());
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
    sqlNodeList = ExtendList()
    {
        return new SqlCreateTable(pos, schemaAndTableName.toString(), sqlNodeList, isNotExist);
    }
}