考勤统计辅助工具，它是用来简化繁琐、易错的考勤统计核对工作，将人工核对打卡记录的工作交给计算机。
它基于[NetBeans 8](https://netbeans.org/)、[JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)及[POI 3](http://poi.apache.org/download.html)
以及一些其他的开源工具包编写而成的。

这个项目是个私人跨平台项目，如果有任何不能接受的行为被发现，请报告至icewee@126.com(PS：请用中文)

## 下载

项目源码可以在[releases](https://github.com/IceWee/kaoqin/releases)下载，
也可以使用GIT克隆到你的本地。

## 简介
运行方法：
本工具为绿色软件，无需安装，双击“运行.bat”即可运行。

使用注意：
由于考勤系统导出的打卡记录是CSV格式文件，本工具无法解析，需要先另存为97-2003版本“.xls”后缀的Excel文件才能处理。

特别提醒：
本工具只统计打卡记录表中正常打卡记录，即上班时间在9点之前以及下班在5点半之后的记录，
这样的记录为正常打卡记录，会在考勤统计时累加出勤天数，早退和迟到的打卡记录不累加，上班和下班无打卡记录的不累加。
使用了百度节假日API后，可以自动识别工作日，如国庆、春节长假后倒休的周六、日为正常工作日，若不打卡会标识异常。

## 执照

MIT © 2016 IceWee
