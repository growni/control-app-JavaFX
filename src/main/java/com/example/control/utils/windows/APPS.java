package com.example.control.utils.windows;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public enum APPS {

    VLC("VideoLAN.VLC", "vlc.exe", false),
    NODEJS("OpenJS.NodeJS", "node.exe", false),
    VSCODE("Microsoft.VisualStudioCode", "Code.exe",  false),
    INTELLIJ("JetBrains.IntelliJIDEA.Community", "idea64.exe",  false),
    GIT("Git.Git", "git.exe",  false),
    DISCORD("Discord.Discord", "Discord.exe",  false),
    STEAM("Valve.Steam", "Steam.exe", false),
    EPICGAMES("EpicGames.EpicGamesLauncher", "EpicGamesLauncher.exe",  false),
    POSTMAN("Postman.Postman", "Postman.exe", false),
    SQLSERVER("Oracle.MySQL", "sqlserver", false),
    MYSQLWORKBENCH("Oracle.MySQLWorkbench", "MySQLWorkbench.exe", false);

    private final String wingetId;
    private final String processName;
    private final boolean requiresAdmin;

    APPS(String wingetId, String processName, boolean requiresAdmin) {
        this.wingetId = wingetId;
        this.processName = processName;
        this.requiresAdmin = requiresAdmin;
    }

    public String getInstallCommand() {
        return "winget install --id=" + wingetId + " -e --accept-package-agreements --silent --disable-interactivity --accept-source-agreements";
    }

    public String getUninstallCommand() {
        return "winget uninstall --id=" + wingetId + " -e --force --all-versions --disable-interactivity --silent";
    }

    public String getKillCommand() {
        return "taskkill /F /IM " + processName;
    }

    public boolean isRequiresAdmin() {
        return requiresAdmin;
    }

    public Set<Character> getDrives() {

        Set<Character> drives = new HashSet<>();
        File[] roots = File.listRoots();

        for (File root : roots) {
            drives.add(root.getPath().charAt(0));
        }

        return drives;
    }

    public Set<Path> getPaths() {

        Path path = null;
        Set<Character> drives = getDrives();
        Set<Path> foundPaths = new HashSet<>();

        for (Character drive : drives) {
            String command = "cmd.exe /c dir " + drive + ":\\ /s /b | findstr /i " + processName;

            try {
                Process process = new ProcessBuilder("cmd.exe", "/c", command).redirectErrorStream(true).start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line = reader.readLine();

                while(line != null) {
                    try {
                        path = Path.of(line);
                        foundPaths.add(path);
                    } catch (IllegalArgumentException | FileSystemNotFoundException e) {
                        System.out.println(e.getMessage());
                    }

                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
        return foundPaths;
    }

//    public void delete(File file) {
//        if (file.isDirectory()) {
//            for (File subFile : file.listFiles()) {
//                delete(subFile);
//            }
//        }
//        file.delete();
//    }
public void delete(Path path) throws IOException {
    Files.walkFileTree(path, new SimpleFileVisitor<>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    });
}
}
