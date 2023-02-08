(() => {
	const tabeauViz = document.getElementById("tableau-viz");
	if (window.innerWidth < 768)
		tabeauViz.setAttribute("device", "phone");
})();