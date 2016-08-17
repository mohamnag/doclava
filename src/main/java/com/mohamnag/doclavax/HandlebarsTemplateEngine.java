package com.mohamnag.doclavax;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.google.doclava.ClassInfo;
import com.google.doclava.PackageInfo;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohamnag on 15/08/16.
 */
public class HandlebarsTemplateEngine implements TemplateEngine {

    private static final Logger logger = LoggerFactory.getLogger(HandlebarsTemplateEngine.class);
    // TODO: 15/08/16 make this a settable param, make sure ends in /
    private static final String OUTPUT_ROOT = "output/hbs/";
    // TODO: 15/08/16 make this a settable param, make sure ends in /
    private static final String INPUT_ROOT = "src/main/resources/templates/";
    // TODO: 15/08/16 make this settable as a param
    private static final String OUTPUT_EXTENSION = ".html";
    // used for meta page outputs that are NOT an index themselves
    private static final String INDEX_OUTPUT_EXTENSION = File.separator + "index" + OUTPUT_EXTENSION;
    private static final String CLASS_TEMPLATE_FILE = "class.hbs";
    private static final String PACKAGE_TEMPLATE_FILE = "package.hbs";
    private final Handlebars handlebars;
    private File inputDir;
    private final Template classPageTemplate;
    private final Template packagePageTemplate;

    public HandlebarsTemplateEngine() throws Exception {
        inputDir = getValidInputDir();
        FileTemplateLoader templateLoader = new FileTemplateLoader(inputDir);
        handlebars = new Handlebars(templateLoader);

        // register JS helpers
        registerJsHelpers(handlebars);

        // compile least needed templates (makes sure they exists)
        classPageTemplate = handlebars.compile(
                CLASS_TEMPLATE_FILE.substring(0, CLASS_TEMPLATE_FILE.length() - 4));

        packagePageTemplate = handlebars.compile(
                PACKAGE_TEMPLATE_FILE.substring(0, PACKAGE_TEMPLATE_FILE.length() - 4));
    }

    private static CharSequence getPathToRoot(String qualifiedName) {
        StringBuilder result = new StringBuilder();
        String[] parts = qualifiedName.split("\\.");

        for (String part : parts) {
            result.append("../");
        }

        return result.toString();
    }

    private static void registerJsHelpers(Handlebars handlebars) throws Exception {
        File helperDir = new File(INPUT_ROOT + "_helpers");
        if (!helperDir.exists() || !helperDir.isDirectory()) {
            logger.warn("Helper dir {} not found, skipping helper registry", helperDir.getCanonicalPath());

        } else {
            File[] helperFiles = helperDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.length() > 3 && name.substring(name.length() - 3).equals(".js");
                }
            });

            for (File helperFile : helperFiles) {
                handlebars.registerHelpers(helperFile);
                logger.debug("Registered helper from {}", helperFile.getCanonicalPath());
            }
        }
    }

    @Override
    public void renderPackagePage(PackageInfo packageInfo, Collection<PackageInfo> packageInfos, Collection<ClassInfo> classInfos) throws IOException {
        HashMap<String, Object> data = new HashMap<>(3);
        data.put("package", packageInfo);
        data.put("packages", packageInfos);
        data.put("classes", classInfos);

        Map<String, Object> meta = new HashMap<>();
        meta.put("pathToRoot", getPathToRoot(packageInfo.qualifiedName()));
        data.put("meta", meta);

        compileContainerPage(
                data,
                packagePageTemplate,
                new File(OUTPUT_ROOT +
                        packageInfo.qualifiedName().replaceAll("\\.", File.separator) +
                        "/index" +
                        OUTPUT_EXTENSION));
    }

    @Override
    public void renderRootClassPage(ClassInfo classInfo, Collection<PackageInfo> packageInfos, Collection<ClassInfo> classInfos) throws IOException {
        HashMap<String, Object> data = new HashMap<>(3);
        data.put("class", classInfo);
        data.put("packages", packageInfos);
        data.put("classes", classInfos);

        Map<String, Object> meta = new HashMap<>();
        meta.put("pathToRoot", getPathToRoot(classInfo.qualifiedName()));
        data.put("meta", meta);

        compileContainerPage(
                data,
                classPageTemplate,
                new File(OUTPUT_ROOT +
                        classInfo.qualifiedName().replaceAll("\\.", File.separator) +
                        "/index" +
                        OUTPUT_EXTENSION));
    }

    @Override
    public void renderMetaPages(Collection<PackageInfo> packageInfos, Collection<ClassInfo> classInfos) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("classes", classInfos);
        data.put("packages", packageInfos);

        Map<String, Object> meta = new HashMap<>();
        data.put("meta", meta);

        // TODO: (LOW) 15/08/16 support recursive inner dirs too
        File[] allTemplates = getAllNonSpecialTemplates(inputDir);

        for (File templateFile : allTemplates) {
            String templateFileName = templateFile.getName();
            String templateFileNameNoExt = templateFileName.substring(0, templateFileName.lastIndexOf('.'));
            Template template = handlebars.compile(templateFileNameNoExt);

            // TODO: (LOW) 17/08/16 pathToRoot should be set based on dir level when recursive inner dirs are supported
            String outputPath = OUTPUT_ROOT + templateFileNameNoExt;
            if (!"index".equals(templateFileNameNoExt)) {
                meta.put("pathToRoot", "../");
                outputPath += INDEX_OUTPUT_EXTENSION;

            } else {
                meta.put("pathToRoot", "");
                outputPath += OUTPUT_EXTENSION;
            }

            compileContainerPage(data, template, new File(outputPath));
        }

        copyAssets(inputDir);

    }

    /**
     * This moves all assets directories (the ones not starting with _) to output.
     *
     * @param inputDir
     */
    private void copyAssets(File inputDir) throws IOException {
        File[] potAssetDirs = inputDir.listFiles(
                (dir, name) -> name.length() > 1 && !name.startsWith("_"));

        Path outputPath = new File(OUTPUT_ROOT).toPath();

        for (File potAssetDir : potAssetDirs) {
            if (potAssetDir.isDirectory()) {
                FileUtils.copyDirectory(
                        potAssetDir,
                        outputPath.resolve(potAssetDir.getName()).toFile());
            }
        }
    }

    private File[] getAllNonSpecialTemplates(File inputDir) {
        return inputDir.listFiles((dir, name) -> {
            // Select all templates except our special cases: class & package
            return (
                    !name.equals(CLASS_TEMPLATE_FILE) &&
                            !name.equals(PACKAGE_TEMPLATE_FILE)
            ) && (
                    name.length() > 4 &&
                            name.substring(name.length() - 4).equals(".hbs")
            );
        });
    }

    private Context getPreparedContext(Object data) {
        return Context
                .newBuilder(data)
                .resolver(
                        SafeMethodValueResolver.INSTANCE,
                        MapValueResolver.INSTANCE,
                        FieldValueResolver.INSTANCE,
                        JavaBeanValueResolver.INSTANCE
                )
                .build();
    }

    private File getValidInputDir() {
        File inputDir = new File(INPUT_ROOT);
        if (!inputDir.exists()) {
            throw new RuntimeException("Input root " + INPUT_ROOT + " does not exist");
        }
        if (!inputDir.isDirectory()) {
            throw new RuntimeException("Input root " + INPUT_ROOT + " is not a directory");
        }
        return inputDir;
    }

    private void compileContainerPage(Object data, Template template, File outputFile) throws IOException {

        Context context = getPreparedContext(data);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new RuntimeException("Could not make the necessary directories for output: " + parentDir.getCanonicalPath());
        }

        FileWriter outputFileWriter = new FileWriter(outputFile);
        template.apply(context, outputFileWriter);
        outputFileWriter.close();

        logger.debug("Compiled and wrote page {}", outputFile.getCanonicalPath());
    }

}
