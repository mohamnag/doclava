<?cs include:"macros.cs" ?>
<!DOCTYPE html>
<html>
<head>
    <title>
        <?cs var:page.title ?> - PROJECT NAME
    </title>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
    <link rel="stylesheet" href="http://www.w3schools.com/lib/w3-theme-teal.css">
</head>

<body>

<nav class="w3-sidenav w3-white w3-card-2" style="width:25%">

    <div class="w3-container w3-teal">
        <h4>PROJECT NAME</h4>
    </div>

    <a href="javascript:void(0)" onclick="w3_close()" class="w3-text-teal w3-hide-large w3-closenav w3-large">
        Close <i class="fa fa-remove"></i>
    </a>

    <div id="index-links">
        <a href="<?cs var:toroot ?>packages.html" class=" w3-padding <?cs if:(page.title == "Package Index") ?>w3-light-grey<?cs /if ?>" >Packages</a>
        <a href="<?cs var:toroot ?>classes.html" class=" w3-padding <?cs if:(page.title == "Class Index") ?>w3-light-grey"<?cs /if ?>" >Classes</a>
    </div>

    <hr/>

    <?cs each:pkg = docs.packages ?>
        <?cs var:pkg.since.key ?>
        <a href="<?cs var:toroot ?><?cs var:pkg.link ?>" class="w3-padding" ><?cs var:pkg.name ?></a>
    <?cs /each ?>

</nav>


<div class="w3-main w3-container" style="margin-left:25%">

    <h1><?cs var:page.title ?></h1>

    <p><?cs call:tag_list(root.descr) ?></p>

    <ul class="w3-ul w3-hoverable">
        <?cs each:pkg = docs.packages ?>
        <li>
            <?cs call:package_link(pkg) ?>
            <?cs if:pkg.since.key ?>[since <?cs var:pkg.since.key ?> ]<?cs /if ?>
            <?cs call:tag_list(pkg.shortDescr) ?>
        </li>
        <?cs /each ?>
    <ul>

</div>

</body>
</html>