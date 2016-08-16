package com.mohamnag.doclavax;

import com.google.doclava.ClassInfo;
import com.google.doclava.PackageInfo;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by mohamnag on 15/08/16.
 */
public interface TemplateEngine {

    /**
     * Renders a single package page with given data containing:
     * - rootClasses: all root classes
     * - packages: all packages
     * - package: current package
     *
     * @param packageInfo  current package
     * @param classInfos   all root classes
     * @param packageInfos all packages
     */
    void renderPackagePage(PackageInfo packageInfo, Collection<PackageInfo> packageInfos, Collection<ClassInfo> classInfos) throws IOException;

    /**
     * Renders a single class page with given data containing:
     * - rootClasses: all root classes
     * - packages: all packages
     * - class: current class
     *
     * @param classInfo    current class
     * @param classInfos   all root classes
     * @param packageInfos all packages
     */
    void renderRootClassPage(ClassInfo classInfo, Collection<PackageInfo> packageInfos, Collection<ClassInfo> classInfos) throws IOException;

    /**
     * Meta pages are any page except the two special cases where we handle separately.
     *
     * @param packageInfos all packages info
     * @param classInfos   all root classes info
     * @throws IOException may be thrown if any IO operation goes wrong
     */
    void renderMetaPages(Collection<PackageInfo> packageInfos, Collection<ClassInfo> classInfos) throws IOException;
}
