window.applyTheme = (theme) => {
	document.documentElement.setAttribute("theme", theme);
	const logoSrc = theme === "dark" ? "https://i.imgur.com/3xp1nLI.png" : "https://i.imgur.com/GPpnszs.png";
	document.getElementsByClassName("logo")[0].src = logoSrc;

	const iconName = theme === "dark" ? "vaadin:sun-o" : "vaadin:moon";
	const themeSwitcher = document.querySelector('vaadin-button');
	const icon = themeSwitcher.querySelector('vaadin-icon[slot="prefix"]');
	icon.setAttribute('icon', iconName);
};

window.applySystemTheme = () => {
	const theme = window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "";
	window.applyTheme(theme);
};

window.switchTheme = () => {
	const theme = document.documentElement.getAttribute("theme") === "dark" ? "" : "dark";
	window.applyTheme(theme);
}

window
	.matchMedia("(prefers-color-scheme: dark)")
	.addEventListener('change', applySystemTheme);
