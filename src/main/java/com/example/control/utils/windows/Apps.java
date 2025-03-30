package com.example.control.utils.windows;

import java.io.File;

public enum Apps {

    ECLIPSE("EclipseAdoptium.Temurin.17.JDK", "eclipse.exe", System.getenv("LOCALAPPDATA") + "\\Microsoft\\WinGet\\Packages\\EclipseFoundation.EclipseIDEforJavaDevelopers_Microsoft.Winget.Source_8wekyb3d8bbwe", false),
    VLC("VideoLAN.VLC", "vlc.exe", System.getenv("LOCALAPPDATA") + "\\Programs\\VideoLAN\\VLC\\vlc.exe", false),
    NODEJS("OpenJS.NodeJS", "node.exe", System.getenv("PROGRAMFILES") + "\\nodejs\\node.exe", true),
    VSCODE("Microsoft.VisualStudioCode", "Code.exe", System.getenv("LOCALAPPDATA") + "\\Programs\\Microsoft VS Code\\Code.exe", false),
    INTELLIJ("JetBrains.IntelliJIDEA.Community", "idea64.exe", System.getenv("LOCALAPPDATA") + "\\JetBrains\\IntelliJ IDEA\\idea64.exe", true),
    GIT("Git.Git", "git.exe", System.getenv("PROGRAMFILES") + "\\Git\\bin\\git.exe", true),
    DISCORD("Discord.Discord", "Discord.exe", System.getenv("LOCALAPPDATA") + "\\Discord\\app\\Discord.exe", false),
    STEAM("Valve.Steam", "Steam.exe", System.getenv("PROGRAMFILES") + "\\Steam\\Steam.exe", true),
    EPICGAMES("EpicGames.EpicGamesLauncher", "EpicGamesLauncher.exe", System.getenv("PROGRAMFILES") + "\\Epic Games\\Launcher\\Portal\\Binaries\\Win64\\EpicGamesLauncher.exe", true),
    BATTLENET("Blizzard.BattleNet", "Battle.net.exe", System.getenv("LOCALAPPDATA") + "\\Battle.net\\Battle.net.exe", false),
    POSTMAN("Postman.Postman", "Postman.exe", System.getenv("LOCALAPPDATA") + "\\Postman", false),
    SQLSERVER("Oracle.MySQL", "sqlserver", System.getenv("LOCALAPPDATA") + "\\Microsoft SQL Server Local DB", false),
    MYSQLWORKBENCH("Oracle.MySQLWorkbench", "mysql-workbench", System.getenv("LOCALAPPDATA") + "\\MySQL\\Workbench", false);

    private final String wingetId;
    private final String processName;
    private final String path;
    private final boolean requiresAdmin;

    Apps(String wingetId, String processName, String path, boolean requiresAdmin) {
        this.wingetId = wingetId;
        this.processName = processName;
        this.path = path;
        this.requiresAdmin = requiresAdmin;
    }

    public String getInstallCommand() {
        return "winget install --id=" + wingetId + " -e --accept-package-agreements --silent --disable-interactivity --accept-source-agreements";
    }

    public String getUninstallCommand() {
        return "winget uninstall --id=" + wingetId + " -e --force --disable-interactivity --silent";
    }

    public String getKillCommand() {
        return "taskkill /F /IM " + processName;
    }

    public boolean isRequiresAdmin() {
        return requiresAdmin;
    }

    public String getPath() {
        return path;
    }

    public boolean doesAppExist() {
        File file = new File(path);
        return file.exists();
    }

    public void delete(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                delete(subFile);
            }
        }
        file.delete();
    }
}
