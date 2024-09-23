# OpenRASP-testcases

较官方测试用例 https://github.com/baidu-security/openrasp-testcases 作出以下改变：
- 针对[官方插件](https://github.com/baidu/openrasp/blob/master/plugins/official/plugin.js)中的每一个算法提供相应测试方法（Apifox/Postman可以导入`rasp.openapi.json`使用）
- 只针对java语言，源码非JSP形式

## 项目说明
项目特意编写了以下几种场景漏洞代码等：
- 展示用户信息：SQL注入漏洞，`GET http://localhost:9999/api/vulns/sqli/users?id=1`
- 执行ping命令并展示结果：命令注入，`GET http://localhost:9999/api/vulns/cmd/ping?address=8.8.8.8`
- 返回对应网络资源：SSRF，`GET http://localhost:9999/api/vulns/ssrf?url=http://localhost:9999`
- 上传序列化文件，解析反序列化后的相关信息：序列化相关漏洞，`POST http://localhost:9999/api/vulns/deserialize/upload --form 'file=@cmd_code.ser'`
- ...

相关反序列化payload已经提前生成，通过`POST http://localhost:9999/api/vulns/deserialize/upload`上传后会触发服务器端不同行为：
- `cmd_code.ser`：命令执行
- `directory_list.ser`：目录遍历
- `file_write.ser`：文件写入

具体可以参看`MaliciousObjectCreator`修改生成其他payload

`fileUpload`相关请求上传的文件，手动生成修改文件后缀为`jsp/html/exe`即可

## 官方插件算法覆盖情况

测试用例覆盖了以下官方算法，算法具体可参看[官方插件](https://github.com/baidu/openrasp/blob/master/plugins/official/plugin.js)：
- SQL注入：
    - 1-sql_userinput
    - 2-sql_policy
    - 3-sql_exception
    - 4-sql_regex
- 文件操作相关：
    - rename_webshell
    - link_webshell
    - 1-fileUpload_webdav(TODO，暂未覆盖)
    - 2-fileUpload_multipart_script
    - 3-fileUpload_multipart_html
    - 4-fileUpload_multipart_exe
    - 1-readFile_userinput
    - 2-readFile_userinput_http(TODO，暂未覆盖)
    - 3-readFile_userinput_unwanted(TODO，暂未覆盖)
    - 4-readFile_outsideWebroot
    - 5-readFile_unwanted
    - 1-writeFile_NTFS
    - 2-writeFile_script
    - 3-writeFile_reflect
    - 1-deleteFile_userinput
    - 1-directory_userinput
    - 2-directory_reflect
    - 3-directory_unwanted
    - 1-include_userinput(TODO，暂未覆盖)
    - 2-include_protocol(TODO，暂未覆盖)
- 命令执行：
    - 1-command_reflect
    - 2-command_userinput
    - 3-command_common
    - 4-command_error
    - 5-command_other
    - 6-command_dnslog
- SSRF
    - 1-ssrf_userinput
    - 2-ssrf_aws
    - 3-ssrf_common
    - 4-ssrf_obfuscate
    - 5-ssrf_protocol
- XXE
    - 1-xxe_disable_entity
    - 2-xxe_protocol
    - 3-xxe_file
- OGNL
    - 1-ognl_blacklist
    - 2-ognl_length_limit
- 反序列化
    - 1-deserialization_blacklist
- DNS相关
    - 1-deserialization_blacklist
- JNDI相关
    - 1-jndi_disable_all 
- 信息泄漏
    - 1-response_dataLeak