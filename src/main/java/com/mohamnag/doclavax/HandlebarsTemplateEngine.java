package com.mohamnag.doclavax;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.google.doclava.ClassInfo;
import com.google.doclava.ContainerInfo;
import com.google.doclava.PackageInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by mohamnag on 15/08/16.
 */
public class HandlebarsTemplateEngine implements TemplateEngine {

    // TODO: 15/08/16 make this a settable param, make sure ends in /
    private static final String OUTPUT_ROOT = "output/hbs/";
    // TODO: 15/08/16 make this a settable param, make sure ends in /
    private static final String INPUT_ROOT = "src/main/resources/templates/";
    // TODO: 15/08/16 make this settable as a param
    private static final String OUTPUT_EXTENSION = ".html";
    private static final String CLASS_TEMPLATE_FILE = "class.hbs";
    private static final String PACKAGE_TEMPLATE_FILE = "package.hbs";
    private final Handlebars handlebars;

    public HandlebarsTemplateEngine() throws IOException {
        handlebars = new Handlebars(new FileTemplateLoader("/"));
    }

    @Override
    public void renderMetaPages(Object data) throws IOException {

        Context context = getPreparedContext(data);
        File inputDir = getValidInputDir();
        // TODO: (LOW) 15/08/16 support recursive inner dirs too
        File[] allTemplates = getAllNonSpecialTemplates(inputDir);

        for (File templateFile : allTemplates) {
            String templateFileName = templateFile.getName();
            String templateFileNameNoExt = templateFileName.substring(0, templateFileName.lastIndexOf('.'));
            Template template = handlebars.compile(templateFile.getParentFile().getCanonicalPath() + File.separator + templateFileNameNoExt);

            String outputPath = OUTPUT_ROOT + templateFileNameNoExt + OUTPUT_EXTENSION;
            FileWriter outputFile = new FileWriter(outputPath);

            template.apply(context, outputFile);
            outputFile.close();

            System.out.println("Compiled " + templateFile.getCanonicalPath() + " to " + outputPath);
        }
    }

    private File[] getAllNonSpecialTemplates(File inputDir) {
        return inputDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // Select all templates except our special cases
                return (
                        !name.equals(CLASS_TEMPLATE_FILE) &&
                                !name.equals(PACKAGE_TEMPLATE_FILE)
                ) && (
                        name.length() > 4 &&
                                name.substring(name.length() - 4).equals(".hbs")
                );
            }
        });
    }

    private Context getPreparedContext(Object data) {
        return Context
                .newBuilder(data)
                .resolver(
                        MethodValueResolver.INSTANCE,
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

    @Override
    public void renderRootClassPages(Collection<ClassInfo> rootClasses) throws IOException {

        File inputDir = getValidInputDir();

        Template template = handlebars.compile(
                inputDir.getCanonicalPath() +
                        File.separator +
                        CLASS_TEMPLATE_FILE.substring(0, CLASS_TEMPLATE_FILE.length() - 4));

        for (ClassInfo classInfo : rootClasses) {
            compileContainerPage(classInfo, template, new File(OUTPUT_ROOT + classInfo.qualifiedName().replaceAll("\\.", File.separator) + OUTPUT_EXTENSION));
        }
    }

    @Override
    public void renderPackagePages(Collection<PackageInfo> packages) throws IOException {

        File inputDir = getValidInputDir();

        Template template = handlebars.compile(
                inputDir.getCanonicalPath() +
                        File.separator +
                        PACKAGE_TEMPLATE_FILE.substring(0, PACKAGE_TEMPLATE_FILE.length() - 4));

        for (PackageInfo packageInfo : packages) {
            compileContainerPage(
                    packageInfo,
                    template,
                    new File(OUTPUT_ROOT +
                            packageInfo.qualifiedName().replaceAll("\\.", File.separator) +
                            File.separator +
                            "index" +
                            OUTPUT_EXTENSION));
        }
    }

    private void compileContainerPage(ContainerInfo packageInfo, Template template, File outputFile) throws IOException {

        Context context = getPreparedContext(packageInfo);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new RuntimeException("Could not make the necessary directories for output: " + parentDir.getCanonicalPath());
        }

        FileWriter outputFileWriter = new FileWriter(outputFile);
        template.apply(context, outputFileWriter);
        outputFileWriter.close();

        System.out.println("Compiled and wrote page " + outputFile.getCanonicalPath());
    }

}
