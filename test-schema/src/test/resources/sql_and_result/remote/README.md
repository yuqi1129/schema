



## 远程测试

remote测试指的是 将本地的测试结果与远程测试结果进行对比，以判断程序的结果是否服务预期。remote 目录下的mysql子目录下的测试内容为远程数据库是MySQL, 即对比本地执行结果与MySQL执行结果。下面就简要说明一下Remote是MySQL是如何测试的 



### 1. 本地

本地共有三个库及表，详细如下：

| 库名 | 表名 |
| :--: | :--: |
| csv  |  t1  |
| json |  t1  |
| yuqi |  t1  |



其中csv, json 类型为文件类型， yuqi为MySQL类型



### 2. MySQL

MySQL 数据库也有三个库， 库表内容如下:


| 库名 | 表名 |
| :--: | :--: |
| csv  |  t1  |
| json |  t1  |
| yuqi |  t1  |



MySQL的shema 与本地schema保持一致, 表中列类型与数据也保持一致。详情请参考[t1.sql](mysql/t1.sql)



### 3. 测试方法

方法:比较本地与远程分别执行一条相同的SQL的结果来判断本地结果是否符号预期，即以MySQL的执行结果为expected result来判断本地执行的 actual result 是否一致。

如一条SQL: select count(*) from csv.t1 a inner join yuqi.t1 b on a.int_type = b.int_type

如果在本地执行，相当于file schema与mysql schema 做join 操作， 假设最后出来的结果是5

如果在远程执行，即在MySQL数据库中执行以上SQL, 这相当于就是是单纯的执行MySQL 语句， 如果结果也是5， 代表项目执行结果兼容纯MySQL, 否则需要改进。 

### 4. 注意事项

remote测试依赖外部数据库, 需要自行配置相关连接配置，否测remote测试无法通过





