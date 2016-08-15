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
     * Meta pages are any page except the two special cases where we handle separately.
     *
     * @param data full data containing both root classes and packages list
     * @throws IOException may be thrown if any IO operation goes wrong
     */
    void renderMetaPages(Object data) throws IOException;

    /**
     * Renders the class page template for each of given root classes.
     *
     * @param rootClasses sorted list of all root classes
     */
    void renderRootClassPages(Collection<ClassInfo> rootClasses) throws IOException;

    /**
     * Renders the package page template for each of given packages.
     *
     * @param packages sorted list of all packages
     */
    void renderPackagePages(Collection<PackageInfo> packages) throws IOException;

}
