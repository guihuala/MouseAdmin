package com.example.adminapi.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BackupService {

    public Path backupDatabase() throws IOException, InterruptedException {
        String backupFileName = "backup_" + System.currentTimeMillis() + ".sql";
        Path backupFilePath = Paths.get(System.getProperty("java.io.tmpdir"), backupFileName);

        // 替换为你的 MySQL 相关信息
        String dbName = "user_management";
        String dbUser = "root";
        String dbPassword = "Dianhoxi22";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "mysqldump",
                "-u" + dbUser,
                "-p" + dbPassword,
                dbName
        );
        processBuilder.redirectOutput(backupFilePath.toFile());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();

        return backupFilePath;
    }
}
