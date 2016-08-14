<?cs include:"partials/page-head.cs" ?>
<?cs include:"macros.cs" ?>
<body class="gc-documentation">

    <div class="g-unit" id="doc-content">

        <div id="jd-header">
        <h1><?cs var:page.title ?></h1>
        </div>

        <div id="jd-content">

            <div class="jd-descr">
            <p><?cs call:tag_list(root.descr) ?></p>
            </div>

            <?cs set:count = #1 ?>
            <table class="jd-sumtable">
                <?cs each:pkg = docs.packages ?>
                    <tr class="<?cs if:count % #2 ?>alt-color<?cs /if ?> api apilevel-<?cs var:pkg.since.key ?>" >
                        <td class="jd-linkcol"><?cs call:package_link(pkg) ?></td>
                        <td class="jd-descrcol" width="100%"><?cs call:tag_list(pkg.shortDescr) ?></td>
                    </tr>
                <?cs set:count = count + #1 ?>
                <?cs /each ?>
            </table>

        </div>
    </div>

    <?cs include:"trailer.cs" ?>

</body>
</html>
