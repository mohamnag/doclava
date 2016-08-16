package com.mohamnag.doclavax;

import com.google.doclava.ClassInfo;
import com.google.doclava.Converter;
import com.google.doclava.PackageInfo;
import com.sun.javadoc.RootDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by mohamnag on 15/08/16.
 */
public class Doclavax {

    private static final Logger logger = LoggerFactory.getLogger(Doclavax.class);

    public static void main(String[] args) {
        System.exit(com.sun.tools.javadoc.Main.execute(args));
    }

    /**
     * The method called by Javadoc after loading class information is done.
     *
     * @param rootDoc object hierarchy that holds class information.
     * @return true or false whether or not the generation was successful
     */
    public static boolean start(RootDoc rootDoc) {

        long startTime = System.nanoTime();
        try {
            Converter.makeInfo(rootDoc);


            ClassInfo[] rootClasses = Converter.rootClasses();

            TreeMap<String, PackageInfo> sortedPackages = new TreeMap<String, PackageInfo>();
            TreeMap<String, ClassInfo> sortedRootClasses = new TreeMap<String, ClassInfo>();

            for (ClassInfo classInfo : rootClasses) {
                PackageInfo pkg = classInfo.containingPackage();
                pkg.addClass(classInfo);

                sortedPackages.put(pkg.name(), pkg);
                sortedRootClasses.put(classInfo.qualifiedName(), classInfo);
            }

            logger.debug("Got {} root classes in {} packages", sortedRootClasses.size(), sortedPackages.size());


            TemplateEngine templateEngine = new HandlebarsTemplateEngine();

            renderClassPages(sortedPackages, sortedRootClasses, templateEngine);
            renderPackagePages(sortedPackages, sortedRootClasses, templateEngine);
            renderMetaPages(sortedPackages, sortedRootClasses, templateEngine);

            long finishTime = System.nanoTime();
            logger.trace("Docs done in " + (finishTime - startTime) / 1000000000.0 + " seconds");
            return true;

        } catch (Exception e) {
            logger.error("Could not generate docs", e);
            return false;
        }
    }

    private static void renderMetaPages(TreeMap<String, PackageInfo> sortedPackages,
                                        TreeMap<String, ClassInfo> sortedRootClasses,
                                        TemplateEngine templateEngine) throws IOException {

        templateEngine.renderMetaPages(sortedPackages.values(), sortedRootClasses.values());
    }

    private static void renderPackagePages(TreeMap<String, PackageInfo> sortedPackages,
                                           TreeMap<String, ClassInfo> sortedRootClasses,
                                           TemplateEngine templateEngine) throws IOException {

        Collection<ClassInfo> classInfos = sortedRootClasses.values();
        Collection<PackageInfo> packageInfos = sortedPackages.values();

        for (PackageInfo packageInfo : sortedPackages.values()) {
            templateEngine.renderPackagePage(packageInfo, packageInfos, classInfos);
        }
    }

    private static void renderClassPages(TreeMap<String, PackageInfo> sortedPackages,
                                         TreeMap<String, ClassInfo> sortedRootClasses,
                                         TemplateEngine templateEngine) throws IOException {

        Collection<ClassInfo> classInfos = sortedRootClasses.values();
        Collection<PackageInfo> packageInfos = sortedPackages.values();

        for (ClassInfo classInfo : classInfos) {
            templateEngine.renderRootClassPage(classInfo, packageInfos, classInfos);
        }
    }


    /**
     * This is used by Javadoc framework to validate the options (including their length).
     *
     * @param option the option label
     * @return length of option or 0 if is not acceptable
     */
    public static int optionLength(String option) {
        if (option.equals("-d")) {
            return 2;
        }
        if (option.equals("-templatedir")) {
            return 2;
        }
        if (option.equals("-hdf")) {
            return 3;
        }
        if (option.equals("-knowntags")) {
            return 2;
        }
        if (option.equals("-toroot")) {
            return 2;
        }
        if (option.equals("-samplecode")) {
            return 4;
        }
        if (option.equals("-htmldir")) {
            return 2;
        }
        if (option.equals("-title")) {
            return 2;
        }
        if (option.equals("-werror")) {
            return 1;
        }
        if (option.equals("-hide")) {
            return 2;
        }
        if (option.equals("-warning")) {
            return 2;
        }
        if (option.equals("-error")) {
            return 2;
        }
        if (option.equals("-keeplist")) {
            return 2;
        }
        if (option.equals("-proofread")) {
            return 2;
        }
        if (option.equals("-todo")) {
            return 2;
        }
        if (option.equals("-public")) {
            return 1;
        }
        if (option.equals("-protected")) {
            return 1;
        }
        if (option.equals("-package")) {
            return 1;
        }
        if (option.equals("-private")) {
            return 1;
        }
        if (option.equals("-hidden")) {
            return 1;
        }
        if (option.equals("-stubs")) {
            return 2;
        }
        if (option.equals("-stubpackages")) {
            return 2;
        }
        if (option.equals("-sdkvalues")) {
            return 2;
        }
        if (option.equals("-apixml")) {
            return 2;
        }
        if (option.equals("-nodocs")) {
            return 1;
        }
        if (option.equals("-parsecomments")) {
            return 1;
        }
        if (option.equals("-since")) {
            return 3;
        }
        if (option.equals("-offlinemode")) {
            return 1;
        }
        if (option.equals("-federate")) {
            return 3;
        }
        if (option.equals("-federationxml")) {
            return 3;
        }
        if (option.equals("-apiversion")) {
            return 2;
        }
        if (option.equals("-assetsdir")) {
            return 2;
        }
        if (option.equals("-generatesources")) {
            return 1;
        }
        if (option.equals("-yaml")) {
            return 2;
        }
        return 0;
    }

}
