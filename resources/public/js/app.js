$(function() {
  var diameter = 1250,
    format = d3.format(",d"),
    color = d3.scale.category20c();

    var bubble = d3.layout.pack()
        .sort(null)
        .size([diameter, diameter])
        .padding(5)
        .value(function(d) {return d.size});

    var svg = d3.select("body").append("svg")
        .attr("width", diameter)
        .attr("height", diameter)
        .attr("class", "bubble");

    var apiurl = "api/v1/transparency-report";

    function drawBubbles(newUrl) {
        //Build the apiurl
        var url = apiurl + newUrl.replace("#","");

        d3.json(url, function(error, root) {

            console.log(processData(root));

            // generate data with calculated layout value
            var nodes = bubble.nodes(processData(root))
                    .filter(function(d) { return !d.children; }); // filter out the outer bubble

            // assign new data to existing DOM
	    var vis = svg.selectAll('circle')
		.data(nodes, function(d) { return d.name; });

	    // enter data -> remove, so non-exist selections for upcoming data won't stay -> enter new data -> ...

	    // To chain transitions,
	    // create the transition on the updating elements before the entering elements
	    // because enter.append merges entering elements into the update selection

	    var duration = 200;
	    var delay = 0;

	    // update - this is created before enter.append. it only applies to updating nodes.
	    vis.transition()
		.duration(duration)
		.delay(function(d, i) {delay = i * 7; return delay;})
		.attr('transform', function(d) { return 'translate(' + d.x + ',' + d.y + ')'; })
		.attr('r', function(d) { return d.r; })
                .select("title").text(function(d) { return d.name + ": " + d.size; })
		.style('opacity', 1); // force to 1, so they don't get stuck below 1 at enter()
                //.attr("dy", ".3em")
                //.style("text-anchor", "middle")
                //.text(function(d) { return d.name.substring(0, d.r / 3); });

	    // enter - only applies to incoming elements (once emptying data)
	    vis.enter().append('circle')
		.attr('transform', function(d) { return 'translate(' + d.x + ',' + d.y + ')'; })
		.attr('r', function(d) { return d.r; })
		.attr('class', function(d) { return d.className; })
                .style("fill", function(d) { return color(d.className); })
                .text(function(d) { return d.name + ": " + d.size; })
		.style('opacity', 0)
		.transition()
		.duration(duration * 1.2)
		.style('opacity', 1);

            vis.append("title")
                .text(function(d) { return d.name + ": " + d.size;});

            vis.append("text")
                .attr("dy", ".3em")
                .style("text-anchor", "middle")
                .text(function(d) { return d.name.substring(0, d.r / 3); });

	    // exit
	    vis.exit()
		.transition()
		.duration(duration + delay)
		.style('opacity', 0)
		.remove();
        });

    }

    function processData(root) {
        var classes = [];
        for(key in root) {
            classes.push({name: key, className: key.toLowerCase(), size: (Math.abs(root[key]))});
        }
        return {children: classes};
    }

    function updateTitle(year, month, column) {
        $("#title-replace").text(year + " ");
    }

    function buildUrl(hash, segment, segment_type) {

        var urlmap = hash.replace("#").split("/");

        console.log(urlmap);

        console.log(segment);

        console.log(segment_type);

        var newUrl = "";

        switch(segment_type) {

        case "year-name":
            newUrl += "#/" + segment + "/" + urlmap[2] + "/" + urlmap[3];
            break;
        case "month-name":
            newUrl += "#/" + urlmap[1] + "/" + segment + "/" + urlmap[3];
            break;
        case "column-name":
            newUrl += "#/" + urlmap[1] + "/" + urlmap[2] + "/" + segment;
            break;

        }
        console.log(newUrl);

        return newUrl;

    }

    function handleClick(event) {

        event.preventDefault();
        var hash = window.location.hash;
        var segment = $(event.target).attr("href").replace("#/", "");
        var segment_type = $(event.target).attr("class");

        //var urlparts = getUrlParts(hash, segment, segment_type);

        var newUrl = buildUrl(hash, segment, segment_type);



        window.location = newUrl;
        drawBubbles(newUrl);

    }

    $("div.menu").delegate('a', 'click', handleClick);

});
