package com.example.testcases.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/vulns/file")
public class FileController {

    @PostMapping("/upload")
    public String upload(HttpServletRequest request) {
        // 解析前先检查请求是否是 multipart 请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            return "请求不包含 multipart 内容";
        }

        // 配置文件上传处理的工厂和上传器
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            // 解析请求，获取所有的表单字段和文件
            List<FileItem> items = upload.parseRequest(request);

            FileItem fileItem = null;
            String dst = null;
            String action = null;

            // 遍历所有表单字段，找出文件、dst 和 action 字段
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // 处理普通表单字段
                    if ("dst".equals(item.getFieldName())) {
                        dst = item.getString();
                    } else if ("action".equals(item.getFieldName())) {
                        action = item.getString();
                    }
                } else {
                    // 处理文件字段
                    fileItem = item;
                }
            }

            // 检查文件是否为空
            if (fileItem == null || fileItem.getSize() == 0) {
                return "上传的文件为空！";
            }

            // 确保目标路径存在
            if (dst == null || dst.isEmpty()) {
                return "目标路径 (dst) 不能为空！";
            }

            Path targetFilePath = Paths.get(dst);
            Path targetDirectory = targetFilePath.getParent();

            // 确保目标父目录存在，如果不存在则创建
            if (targetDirectory != null) {
                if (!Files.exists(targetDirectory)) {
                    Files.createDirectories(targetDirectory);
                }
            }

            // 如果目标文件已存在，则删除
            if (Files.exists(targetFilePath)) {
                Files.delete(targetFilePath);
            }

            // 将文件存储到临时目录
            Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), fileItem.getName());
            Files.write(tempFilePath, fileItem.get());

            // 根据 action 参数执行不同操作
            if ("move".equalsIgnoreCase(action)) {
                // 执行文件移动操作
                Files.move(tempFilePath, targetFilePath);
                return "文件已成功上传并移动到：" + targetFilePath;
            } else if ("link".equalsIgnoreCase(action)) {
                // 执行硬链接操作
                Files.createLink(targetFilePath, tempFilePath);
                return "文件已成功上传并创建硬链接：" + targetFilePath;
            } else if ("create".equalsIgnoreCase(action)) {
                if (Files.exists(targetFilePath)) {
                    Files.delete(targetFilePath);  // 删除已有文件
                }
                Files.createFile(targetFilePath);
                try (OutputStream os = Files.newOutputStream(targetFilePath)) {
                    os.write(fileItem.get());
                }
                return "文件已成功创建并写入：" + targetFilePath;
            } else {
                return "未知的操作类型！请选择 'move' 或 'link'";
            }

        } catch (FileUploadException | org.apache.commons.fileupload.FileUploadException e) {
            e.printStackTrace();
            return "文件上传失败：" + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "文件操作失败：" + e.getMessage();
        }
    }

//    @PostMapping("/upload")
//    public String uploadFileSpring(@RequestParam("file") MultipartFile file,
//                                   @RequestParam("dst") String dst,
//                                   @RequestParam("action") String action
//
//    ) {
//        if (file.isEmpty()) {
//            return "上传的文件为空！";
//        }
//        try {
//            // 目标文件路径和父目录
//            Path targetFilePath = Paths.get(dst);
//            Path targetDirectory = targetFilePath.getParent();
//
//            // 确保目标父目录存在，如果不存在则创建
//            if (targetDirectory != null) {
//                if (!Files.exists(targetDirectory)) {
//                    Files.createDirectories(targetDirectory);
//                }
//            }
//
//            // 将文件存储到临时目录
//            Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
//            Files.write(tempFilePath, file.getBytes());
//
//            // 根据 action 参数执行不同操作
//            if ("move".equalsIgnoreCase(action)) {
//                // 执行文件移动操作
//                Files.move(tempFilePath, targetFilePath);
//                return "文件已成功上传并移动到：" + targetFilePath.toString();
//            } else if ("link".equalsIgnoreCase(action)) {
//                // 执行硬链接操作
//                Files.createLink(targetFilePath, tempFilePath);
//                return "文件已成功上传并创建硬链接：" + targetFilePath.toString();
//            } else if ("create".equalsIgnoreCase(action)) {
//                if (Files.exists(targetFilePath)) {
//                    Files.delete(targetFilePath);  // 删除已有文件
//                }
//                Files.createFile(targetFilePath);
//                try (OutputStream os = Files.newOutputStream(targetFilePath)) {
//                    os.write(file.getBytes());
//                }
//                return "文件已成功创建并写入：" + targetFilePath.toString();
//            } else {
//                return "未知的操作类型！请选择 'move' 或 'link'";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "文件上传或硬链接创建失败：" + e.getMessage();
//        }
//    }

    @PostMapping("/upload2")
    public String uploadFileApache(HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        File uploadedFile = new File(fileName);
                        item.write(uploadedFile);
                        return "File uploaded successfully: " + fileName;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error processing file upload: " + e.getMessage();
            }
        }
        return "No files uploaded";
    }

    @GetMapping("/read")
    public String readFile(@RequestParam("path") String filePath) {
        FileInputStream fis = null;
        try {
            // 获取文件对象
            File file = new File(filePath);

            // 检查文件是否存在
            if (!file.exists()) {
                return "文件不存在: " + filePath;
            }

            // 创建 FileInputStream 读取文件
            fis = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()]; // 根据文件大小创建字节数组

            // 读取文件内容到字节数组中
            int bytesRead = fis.read(fileData);
            if (bytesRead == -1) {
                return "无法读取文件内容: " + filePath;
            }
            // 将字节数组转换为字符串并返回
            return new String(fileData, StandardCharsets.UTF_8);

            // 还可以通过其他方式，读取文件内容并返回 return new String(Files.readAllBytes(path));


        } catch (IOException e) {
            // 处理文件读取时的异常并返回错误信息
            e.printStackTrace();
            return "读取文件失败: " + e.getMessage();
        }
    }

    @PostMapping("/write")
    public String writeFile(@RequestParam("content") String content,
                            @RequestParam("dst") String dst) {
        // 将目标路径转换为 File 对象
        File file = new File(dst);

        // 检查目标文件的父目录是否存在，不存在则创建
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        // 使用 BufferedWriter 写入文件
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
//            writer.write(content);
//            return "文件成功写入: " + file.getAbsolutePath();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "写入文件失败: " + e.getMessage();
//        }
        // 使用 FileOutputStream 写入文件
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 将内容转换为字节数组并写入文件
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            fos.write(contentBytes);
            return "文件成功写入: " + file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "写入文件失败: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("path") String filePath) {
        File file = new File(filePath);

//        // 检查文件是否存在
//        if (!file.exists()) {
//            return "文件不存在: " + filePath;
//        }
//
//        // 检查文件是否是一个文件，而不是目录
//        if (!file.isFile()) {
//            return "不是一个有效的文件: " + filePath;
//        }

        // 尝试删除文件
        boolean deleted = file.delete();
        if (deleted) {
            return "文件已成功删除: " + filePath;
        } else {
            // 如果文件没有被删除，可能是因为文件被其他程序使用或者权限问题
            return "无法删除文件: " + filePath;
        }
    }

    @GetMapping("/list")
    public String listDirectory(@RequestParam("path") String directoryPath) {
        File directory = new File(directoryPath);

        // 检查路径是否存在
        if (!directory.exists()) {
            return "目录不存在: " + directoryPath;
        }

        // 检查是否是一个目录
        if (!directory.isDirectory()) {
            return "指定路径不是一个目录: " + directoryPath;
        }

        // 获取目录下所有文件和文件夹的名称
        String[] files = directory.list();
        if (files == null || files.length == 0) {
            return "目录为空: " + directoryPath;
        }

        // 创建一个格式化的字符串来显示目录内容
        StringBuilder builder = new StringBuilder("目录内容 [" + directoryPath + "]:\n");
        for (String file : files) {
            builder.append(file).append("\n");
        }

        return builder.toString();
    }
}
