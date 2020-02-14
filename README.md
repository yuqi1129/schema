







# Schema 一款简单支持异构数据源的SQL Driver



## 1. 简介

schema 项目是一个简单的union-sql工程， 即用一套SQL可以同时访问不同数据源的数据，做到跨数据源JOIN等复杂操作，目前只简单支持以下数据源



- [x] File(本地文件)
- [x] MySQL
- [ ] HBase
- [ ] Hive
- [ ] ....



像HBase、Hive等其它数据源待慢慢支持，欢迎感兴趣的小伙伴一起补充完善。



## 2. 主要技术点

本项目主要用到了Calcite 中avatica driver, 一个通用的JAVA jdbc Driver 框架，其主要特点在于可以自行适配不同的数据源， 比如文件、MySQL、Hbase 等不同数据源；

另一个技术点为Apache Calcite, 关于Apache Calcite 的作用及基本用法请参考我的[简书](https://www.jianshu.com/p/2dfbd71b7f0f)

执行引擎是通过Calcite 内置的CodeGen模块动态生成代码并本地执行，关于这一块是一个大优化方向，比如说可以用一些分布式执行引擎如Flink 批、Spark引擎； 还可以进一步优化DAG 执行引擎，采用MPP架构方法，提高执行效率



### 3. 如何使用



#### 3.1 File 数据源



首先进入file-shema的resource目录下， 其中有json和csv两个文件夹，表示支持json和csv两种文件格式，每个目录下有.csv(.json)和.schema文件分别表为数据文件及schema文件，具体内容就不一一阐述，请参考其文件内容

接着需要配置schema元数据，请参考file.json文件内容

然后可以运行FileSelect 进行测试



#### 3.2 MySQL 数据源

首先需要一个可以访问的MySQL数据库， 接着在mysql-schema的resource下有一个mysql.json schema元数据文件， 配置正确的mysql数据库相关配置即可

接着可以直接运行MysqlSelect 即可



#### 3.3 File 与MySQL结合

可以将File与MySQL数据源结合，做Join等关联操作，详情请参考test-schema中的FileAndMysqlScheamTest



### 4. 集成测试

你可以直接运行:
```
mvn verify 
```
来起动集成测试，该继集成主要用来测试SQL执行结果的正确性。全部的测试类都在test-schema的test目录中，当然你也可以直接运行`com.yuqi.schema.common.integrate`
目录下的类文件来进行测试

### 5. TODO

目前本项目属于刚开发阶段，有很多不足之处:

- 代码规范
- 测试
- 功能
- ...

以上不足之处会在后期慢慢改善，欢迎感兴趣的小伙伴一起参与此[项目](https://github.com/yuqi1129/schema)

















