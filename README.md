# 前后端分离项目简单案例

技术构成：SpringBoot+React+React-Router+Antd+Redux(未连接数据库)

#部署

- 安装eclipse+maven环境,idea也可以。
- react项目打包：npm run build
- 找到web项目的webapp目录

![找到webapp目录](https://upload-images.jianshu.io/upload_images/1131704-d9187bceab34a7f1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

注意：WEB-INF目录和MP_verify_BMkQAHplMGROBlAn.txt文件不能删除。

把react项目打包好的build目录下的文件拷贝到web项目中的webapp目录（不要删除webapp目录下已经有的文件和目录）：

#打包

- 对web项目执行命令： maven install


![执行maven install命令](https://upload-images.jianshu.io/upload_images/1131704-9b44b29e1a1398c6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![image.png](https://upload-images.jianshu.io/upload_images/1131704-bb06f7ca483a9072.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

打包的过程中：注意Console控制台 出现BUILD SUCCESS 提示语句之后，maven打包才是正确生成，如果出现错误，请自行百度错误提示。


找到target目录下，拷贝war文件:

![image.png](https://upload-images.jianshu.io/upload_images/1131704-04dddbdaf647deda.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

最后一步就是把war文件发布到linux中的web容器中
