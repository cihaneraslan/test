moka
====

moonlightol

该工程下提供基础web研发环境

======================================================================================
==说明：
==1.每次更新请按如下方式记录更新信息
==2.设计/使用文档请放在：svn://10.0.0.245/hudongDocument/tech-1/中心工程/keel
======================================================================================
Version:1.0.1.20120730
修改日期：2012-07-30
问题描述：替换新的日志框架
新增功能：无
更新功能：替换所有类中的日志打印方式，采用新封装的日志框架(hudonglogger)
设计/使用文档：无
是否向下兼容：是
======================================================================================
Version：keel.web.filter.version:1.0.1.20121113
修改日期：2012-11-13
问题描述：增加用户行为记录过滤器
新增功能：增加用户行为记录过滤器，只有需要记录用户行为的记录才会使用此过滤器，并且此过滤器会对url进行筛选。
更新功能：无
设计/使用文档：无
是否向下兼容：是
======================================================================================

///////////////// 研发环境 ////////////////////////////////////////
清空环境：
mvn clean -DAPP_ENV=dev

建立Eclipse环境：
mvn eclipse:eclipse -DAPP_ENV=dev

清空Eclipse环境：
mvn eclipse:clean -DAPP_ENV=dev

只编译：
mvn compile -DAPP_ENV=dev

只打包：
mvn package -DAPP_ENV=dev  -Dmaven.test.skip=true

只装配：
mvn process-resources -DAPP_ENV=dev

打包+装配+安装二进制：
mvn install -DAPP_ENV=dev -Dmaven.test.skip=true

///////////////// 测试环境 ////////////////////////////////////////

清空环境：
mvn clean -DAPP_ENV=test

打包+装配+安装二进制：
mvn install -DAPP_ENV=test -Dmaven.test.skip=true

mvn clean install -DAPP_ENV=idc_test -Dmaven.test.skip=true

///////////////// 生产环境 ////////////////////////////////////////

清空环境：
mvn clean -DAPP_ENV=prod

打包+装配+安装二进制：
mvn install -DAPP_ENV=prod -Dmaven.test.skip=true


nohup ./httpd.sh  -server a >nohup.log 2>&1 &
