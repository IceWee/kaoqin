# kaoqin
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://192.168.1.101/weibing/HxdPluginClient/blob/master/LICENSE)
[![OpenTracing Badge](https://img.shields.io/badge/OpenTracing-enabled-blue.svg)](http://opentracing.io)

## 简介
>走向未来®考勤统计辅助工具，用来简化繁琐、易错的考勤统计核对工作，将人工核对打卡记录的工作交给计算机。
基于[NetBeans 8.2](https://netbeans.org/)、[JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)及[POI 3](http://poi.apache.org/download.html)
及一些其他的开源工具包编写而成。
该项目属于私人学习项目，如果发现有任何不能接受的行为，请发邮件至[icewee@126.com](mailto:icewee@126.com?subject=kaoqin Feedback)。

## 下载
>项目源码需要使用[GIT克隆](https://github.com/IceWee/kaoqin.git)到你的本地。

## 构建

#### 1.克隆项目到本地
```Perl
git clone https://github.com/IceWee/kaoqin.git
```
#### 2.进入项目目录
```Perl
cd ./kaoqin
```
#### 3.安装Swing皮肤
```Perl
mvn install:install-file -Dfile=./src/main/lib/substance-4.0.jar -DgroupId=org.jvnet -DartifactId=substance -Dversion=4.0.RELEASE -Dpackaging=jar
```
#### 4.MAVEN打包
```Perl
mvn package
```
#### 5.运行工具
>到`kaoqin/target/kaoqin`目录下，双击`kaoqin-1.0.jar`即可启动本工具。

## 其他
####使用注意
>由于考勤系统导出的打卡记录是CSV格式文件，本工具无法解析，需要先另存为Excel文件才能处理。

####特别提醒
>本工具只统计打卡记录表中的正常打卡记录，即上班时间在9点之前以及下班在5点半之后的记录，
只有这样的记录才认为是为正常打卡，会在考勤统计时累加出勤天数，早退和迟到的打卡记录不累加，上班和下班无打卡记录的不累加。
使用了百度节假日API后，可以自动识别工作日，如国庆、春节长假后倒休的周六、日为正常工作日，若不打卡会标识异常。

## 版权
>MIT © 2017 `IceWee`
