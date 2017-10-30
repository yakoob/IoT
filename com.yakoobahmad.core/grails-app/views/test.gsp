<!doctype html>
<html>

<body>

<div id="content" role="main">
    <section class="row colset-2-its">

        <div id="controllers" role="navigation">
            <h2>Available Pumpkins:</h2>
            <ul>
                <g:each var="video" in="${videos}">
                    <li class="controller">
                        <g:link controller="test" action="play" params="[video:video.name.name().toUpperCase()]">${video.name.name()}</g:link>
                    </li>
                </g:each>
            </ul>
        </div>

        <div id="holograms" role="navigation">
            <h2>Available Holograms:</h2>
            <ul>
                <g:each var="hologram" in="${holograms}">
                    <li class="controller">
                        <g:link controller="test" action="play" params="[video:hologram.name.name().toUpperCase()]">${hologram.name.name()}</g:link>
                    </li>
                </g:each>
            </ul>
        </div>
    </section>
</div>

</body>
</html>
