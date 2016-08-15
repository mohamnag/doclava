/**
 * A helper that builds up a link from given full qualified
 * package or class name.
 *
 * Created by mohamnag on 15/08/16.
 */

Handlebars.registerHelper('ntl', function (context) {
    return context.split(".").join("/");
});