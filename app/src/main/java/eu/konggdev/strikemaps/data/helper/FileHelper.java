package eu.konggdev.strikemaps.data.helper;

import android.content.res.AssetManager;
import android.os.Environment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import eu.konggdev.strikemaps.app.AppController;

//FIXME: Ugly
public final class FileHelper {
    public static String loadStringFromAssetFile(String filePath, AppController app) {
        try (InputStream is = app.getActivity().getAssets().open(filePath)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String loadStringFromUserFile(String filePath) {
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file)) {
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String[] getAssetFiles(String path, String fileExt, AppController app) {
        AssetManager assetManager = app.getActivity().getAssets();
        try {
            if (path != null && path.startsWith("/")) {
                path = path.substring(1);
            }

            String fullPath = (path == null || path.isEmpty()) ? "" : path;

            String[] files = assetManager.list(fullPath);

            if (files == null) return new String[0];

            if (fileExt == null || fileExt.isEmpty())
                return files;

            List<String> filtered = new ArrayList<>();
            for (String file : files) {
                if (file.toLowerCase().endsWith(fileExt.toLowerCase())) {
                    filtered.add((fullPath.isEmpty() ? "" : fullPath + "/") + file);
                }
            }

            return filtered.toArray(new String[0]);

        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static InputStream openAssetStream(String path, AppController app) {
        try {
            return app.getActivity().getAssets().open(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getUserFiles(String path, String fileExt, AppController app) {
        String packageName = app.getActivity().getPackageName();
        File userDirectory = new File(Environment.getExternalStorageDirectory(), "Android/data/" + packageName + "/" + path);

        if (!userDirectory.exists() || !userDirectory.isDirectory())
            return new String[0];

        File[] files = userDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (fileExt == null || fileExt.isEmpty()) {
                    return true;
                }

                return filename.toLowerCase().endsWith(fileExt.toLowerCase());
            }
        });

        if (files == null || files.length == 0) {
            return new String[0];
        }

        List<String> fileList = new ArrayList<>();
        for (File file : files) {
            fileList.add(file.getAbsolutePath());
        }

        return fileList.toArray(new String[0]);
    }
}
