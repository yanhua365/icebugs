icebugs
====
本项目主要是利用Findbugs和SVN来发现新增代码中的潜在编码问题的。

# 原理

可以使用Findbugs对代码做静态代码解析，输出为XML格式，和SVN的diff文件做对比，就可以确定哪些改动的代码有问题。


# 技术

本项目使用Groovy语言来开发，主要是因为Groovy里可以内嵌Ant的指令，而Findbugs和SVN都有成熟的Ant扩展可用。同时，Groovy语言语法兼容Java语言，但又更为轻巧。


# 使用

把本项目的脚本方在项目的icebugs目录下，执行脚本就会在控制台上输出检查的结果了。