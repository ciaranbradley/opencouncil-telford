$(function() {
  var diameter = 1300,
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


d3.json("api/v1/transparency-report/2014/service-delivery-area", function(error, root) {
  console.log(classes(root));
  var node = svg.selectAll(".node")
      .data(bubble.nodes(classes(root))
      .filter(function(d) { return !d.children; }))
    .enter().append("g")
      .attr("class", "node")
      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

  node.append("title")
      .text(function(d) { return d.name + ": " + d.size; });

  node.append("circle")
      .attr("r", function(d) { return d.r; })
      .style("fill", function(d) { return color(d.className); });
      //.style("fill", function(d) { return "#DF4949;" });
  node.append("text")
      .attr("dy", ".3em")
      .style("text-anchor", "middle")
      .text(function(d) { return d.name.substring(0, d.r / 3); });
});

// Returns a flattened hierarchy containing all leaf nodes under the root.
function classes(root) {
  var classes = [];
  for(key in root) {
    classes.push({name: key, className: key.toLowerCase(), size: (Math.abs(root[key]))});
  }
  return {children: classes};
}

d3.select(self.frameElement).style("width", diameter + "px").style("height", 800 + "px");
});
