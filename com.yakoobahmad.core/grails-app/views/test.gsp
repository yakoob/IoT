<!doctype html>
<html>

<body>

<div id="content" role="main">
    <section class="row colset-2-its">

        <div id="controllers" role="navigation">
            <h2>Available Videos:</h2>
            <ul>
                <g:each var="video" in="${videos}">
                    <li class="controller">
                        <g:link controller="test" action="play" params="[video:video.name.name().toUpperCase()]">${video.name.name()}</g:link>
                    </li>
                </g:each>
            </ul>
        </div>
    </section>
</div>

</body>
</html>
