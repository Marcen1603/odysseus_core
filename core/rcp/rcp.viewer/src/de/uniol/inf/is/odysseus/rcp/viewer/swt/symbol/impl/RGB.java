package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import java.util.HashMap;
import java.util.Map;

public class RGB {

	public static enum X11Col {
		AliceBlue, BlueViolet, CadetBlue, CadetBlue1, CadetBlue2, CadetBlue3, CadetBlue4, CornflowerBlue, DarkBlue, DarkCyan, DarkSlateBlue, DarkTurquoise, DeepSkyBlue, DeepSkyBlue1, DeepSkyBlue2, DeepSkyBlue3, DeepSkyBlue4, DodgerBlue, DodgerBlue1, DodgerBlue2, DodgerBlue3, DodgerBlue4, LightBlue, LightBlue1, LightBlue2, LightBlue3, LightBlue4, LightCyan, LightCyan1, LightCyan2, LightCyan3, LightCyan4, LightSkyBlue, LightSkyBlue1, LightSkyBlue2, LightSkyBlue3, LightSkyBlue4, LightSlateBlue, LightSteelBlue, LightSteelBlue1, LightSteelBlue2, LightSteelBlue3, LightSteelBlue4, MediumAquamarine, MediumBlue, MediumSlateBlue, MediumTurquoise, MidnightBlue, NavyBlue, PaleTurquoise, PaleTurquoise1, PaleTurquoise2, PaleTurquoise3, PaleTurquoise4, PowderBlue, RoyalBlue, RoyalBlue1, RoyalBlue2, RoyalBlue3, RoyalBlue4, SkyBlue, SkyBlue1, SkyBlue2, SkyBlue3, SkyBlue4, SlateBlue, SlateBlue1, SlateBlue2, SlateBlue3, SlateBlue4, SteelBlue, SteelBlue1, SteelBlue2, SteelBlue3, SteelBlue4, aquamarine, aquamarine1, aquamarine2, aquamarine3, aquamarine4, azure, azure1, azure2, azure3, azure4, blue, blue1, blue2, blue3, blue4, cyan, cyan1, cyan2, cyan3, cyan4, navy, turquoise, turquoise1, turquoise2, turquoise3, turquoise4,

		RosyBrown, RosyBrown1, RosyBrown2, RosyBrown3, RosyBrown4, SaddleBrown, SandyBrown, beige, brown, brown1, brown2, brown3, brown4, burlywood, burlywood1, burlywood2, burlywood3, burlywood4, chocolate, chocolate1, chocolate2, chocolate3, chocolate4, peru, tan, tan1, tan2, tan3, tan4,

		DarkGreen, DarkKhaki, DarkOliveGreen, DarkOliveGreen1, DarkOliveGreen2, DarkOliveGreen3, DarkOliveGreen4, DarkSeaGreen, DarkSeaGreen1, DarkSeaGreen2, DarkSeaGreen3, DarkSeaGreen4, ForestGreen, GreenYellow, LawnGreen, LightGreen, LightSeaGreen, LimeGreen, MediumSeaGreen, MediumSpringGreen, MintCream, OliveDrab, OliveDrab1, OliveDrab2, OliveDrab3, OliveDrab4, PaleGreen, PaleGreen1, PaleGreen2, PaleGreen3, PaleGreen4, SeaGreen, SeaGreen1, SeaGreen2, SeaGreen3, SeaGreen4, SpringGreen, SpringGreen1, SpringGreen2, SpringGreen3, SpringGreen4, YellowGreen, chartreuse, chartreuse1, chartreuse2, chartreuse3, chartreuse4, green, green1, green2, green3, green4, khaki, khaki1, khaki2, khaki3, khaki4,

		DarkOrange, DarkOrange1, DarkOrange2, DarkOrange3, DarkOrange4, DarkSalmon, LightCoral, LightSalmon, LightSalmon1, LightSalmon2, LightSalmon3, LightSalmon4, PeachPuff, PeachPuff1, PeachPuff2, PeachPuff3, PeachPuff4, bisque, bisque1, bisque2, bisque3, bisque4, coral, coral1, coral2, coral3, coral4, honeydew, honeydew1, honeydew2, honeydew3, honeydew4, orange, orange1, orange2, orange3, orange4, salmon, salmon1, salmon2, salmon3, salmon4, sienna, sienna1, sienna2, sienna3, sienna4,

		DarkRed, DeepPink, DeepPink1, DeepPink2, DeepPink3, DeepPink4, HotPink, HotPink1, HotPink2, HotPink3, HotPink4, IndianRed, IndianRed1, IndianRed2, IndianRed3, IndianRed4, LightPink, LightPink1, LightPink2, LightPink3, LightPink4, MediumVioletRed, MistyRose, MistyRose1, MistyRose2, MistyRose3, MistyRose4, OrangeRed, OrangeRed1, OrangeRed2, OrangeRed3, OrangeRed4, PaleVioletRed, PaleVioletRed1, PaleVioletRed2, PaleVioletRed3, PaleVioletRed4, VioletRed, VioletRed1, VioletRed2, VioletRed3, VioletRed4, firebrick, firebrick1, firebrick2, firebrick3, firebrick4, pink, pink1, pink2, pink3, pink4, red, red1, red2, red3, red4, tomato, tomato1, tomato2, tomato3, tomato4,

		gray0, gray10, gray20, gray30, gray40, gray50, gray60, gray70, gray80, gray90, gray100,

		BlanchedAlmond, DarkGoldenrod, DarkGoldenrod1, DarkGoldenrod2, DarkGoldenrod3, DarkGoldenrod4, LemonChiffon, LemonChiffon1, LemonChiffon2, LemonChiffon3, LemonChiffon4, LightGoldenrod, LightGoldenrod1, LightGoldenrod2, LightGoldenrod3, LightGoldenrod4, LightGoldenrodYellow, LightYellow, LightYellow1, LightYellow2, LightYellow3, LightYellow4, PaleGoldenrod, PapayaWhip, cornsilk, cornsilk1, cornsilk2, cornsilk3, cornsilk4, gold, gold1, gold2, gold3, gold4, goldenrod, goldenrod1, goldenrod2, goldenrod3, goldenrod4, moccasin, yellow, yellow1, yellow2, yellow3, yellow4,

	}

	// from http://www.farb-tabelle.de/de/farbtabelle.htm
	static public Map<X11Col, RGB> x11Colors = new HashMap<>();
	static {
		x11Colors.put(X11Col.AliceBlue, new RGB(240, 248, 255));
		x11Colors.put(X11Col.BlueViolet, new RGB(138, 43, 226));
		x11Colors.put(X11Col.CadetBlue, new RGB(95, 158, 160));
		x11Colors.put(X11Col.CadetBlue1, new RGB(152, 245, 255));
		x11Colors.put(X11Col.CadetBlue2, new RGB(142, 229, 238));
		x11Colors.put(X11Col.CadetBlue3, new RGB(122, 197, 205));
		x11Colors.put(X11Col.CadetBlue4, new RGB(83, 134, 139));
		x11Colors.put(X11Col.CornflowerBlue, new RGB(100, 149, 237));
		x11Colors.put(X11Col.DarkBlue, new RGB(0, 0, 139));
		x11Colors.put(X11Col.DarkCyan, new RGB(0, 139, 139));
		x11Colors.put(X11Col.DarkSlateBlue, new RGB(72, 61, 139));
		x11Colors.put(X11Col.DarkTurquoise, new RGB(0, 206, 209));
		x11Colors.put(X11Col.DeepSkyBlue, new RGB(0, 191, 255));
		x11Colors.put(X11Col.DeepSkyBlue1, new RGB(0, 191, 255));
		x11Colors.put(X11Col.DeepSkyBlue2, new RGB(0, 178, 238));
		x11Colors.put(X11Col.DeepSkyBlue3, new RGB(0, 154, 205));
		x11Colors.put(X11Col.DeepSkyBlue4, new RGB(0, 104, 139));
		x11Colors.put(X11Col.DodgerBlue, new RGB(30, 144, 255));
		x11Colors.put(X11Col.DodgerBlue1, new RGB(30, 144, 255));
		x11Colors.put(X11Col.DodgerBlue2, new RGB(28, 134, 238));
		x11Colors.put(X11Col.DodgerBlue3, new RGB(24, 116, 205));
		x11Colors.put(X11Col.DodgerBlue4, new RGB(16, 78, 139));
		x11Colors.put(X11Col.LightBlue, new RGB(173, 216, 230));
		x11Colors.put(X11Col.LightBlue1, new RGB(191, 239, 255));
		x11Colors.put(X11Col.LightBlue2, new RGB(178, 223, 238));
		x11Colors.put(X11Col.LightBlue3, new RGB(154, 192, 205));
		x11Colors.put(X11Col.LightBlue4, new RGB(104, 131, 139));
		x11Colors.put(X11Col.LightCyan, new RGB(224, 255, 255));
		x11Colors.put(X11Col.LightCyan1, new RGB(224, 255, 255));
		x11Colors.put(X11Col.LightCyan2, new RGB(209, 238, 238));
		x11Colors.put(X11Col.LightCyan3, new RGB(180, 205, 205));
		x11Colors.put(X11Col.LightCyan4, new RGB(122, 139, 139));
		x11Colors.put(X11Col.LightSkyBlue, new RGB(135, 206, 250));
		x11Colors.put(X11Col.LightSkyBlue1, new RGB(176, 226, 255));
		x11Colors.put(X11Col.LightSkyBlue2, new RGB(164, 211, 238));
		x11Colors.put(X11Col.LightSkyBlue3, new RGB(141, 182, 205));
		x11Colors.put(X11Col.LightSkyBlue4, new RGB(96, 123, 139));
		x11Colors.put(X11Col.LightSlateBlue, new RGB(132, 112, 255));
		x11Colors.put(X11Col.LightSteelBlue, new RGB(176, 196, 222));
		x11Colors.put(X11Col.LightSteelBlue1, new RGB(202, 225, 255));
		x11Colors.put(X11Col.LightSteelBlue2, new RGB(188, 210, 238));
		x11Colors.put(X11Col.LightSteelBlue3, new RGB(162, 181, 205));
		x11Colors.put(X11Col.LightSteelBlue4, new RGB(110, 123, 139));
		x11Colors.put(X11Col.MediumAquamarine, new RGB(102, 205, 170));
		x11Colors.put(X11Col.MediumBlue, new RGB(0, 0, 205));
		x11Colors.put(X11Col.MediumSlateBlue, new RGB(123, 104, 238));
		x11Colors.put(X11Col.MediumTurquoise, new RGB(72, 209, 204));
		x11Colors.put(X11Col.MidnightBlue, new RGB(25, 25, 112));
		x11Colors.put(X11Col.NavyBlue, new RGB(0, 0, 128));
		x11Colors.put(X11Col.PaleTurquoise, new RGB(175, 238, 238));
		x11Colors.put(X11Col.PaleTurquoise1, new RGB(187, 255, 255));
		x11Colors.put(X11Col.PaleTurquoise2, new RGB(174, 238, 238));
		x11Colors.put(X11Col.PaleTurquoise3, new RGB(150, 205, 205));
		x11Colors.put(X11Col.PaleTurquoise4, new RGB(102, 139, 139));
		x11Colors.put(X11Col.PowderBlue, new RGB(176, 224, 230));
		x11Colors.put(X11Col.RoyalBlue, new RGB(65, 105, 225));
		x11Colors.put(X11Col.RoyalBlue1, new RGB(72, 118, 255));
		x11Colors.put(X11Col.RoyalBlue2, new RGB(67, 110, 238));
		x11Colors.put(X11Col.RoyalBlue3, new RGB(58, 95, 205));
		x11Colors.put(X11Col.RoyalBlue4, new RGB(39, 64, 139));
		x11Colors.put(X11Col.SkyBlue, new RGB(135, 206, 235));
		x11Colors.put(X11Col.SkyBlue1, new RGB(135, 206, 255));
		x11Colors.put(X11Col.SkyBlue2, new RGB(126, 192, 238));
		x11Colors.put(X11Col.SkyBlue3, new RGB(108, 166, 205));
		x11Colors.put(X11Col.SkyBlue4, new RGB(74, 112, 139));
		x11Colors.put(X11Col.SlateBlue, new RGB(106, 90, 205));
		x11Colors.put(X11Col.SlateBlue1, new RGB(131, 111, 255));
		x11Colors.put(X11Col.SlateBlue2, new RGB(122, 103, 238));
		x11Colors.put(X11Col.SlateBlue3, new RGB(105, 89, 205));
		x11Colors.put(X11Col.SlateBlue4, new RGB(71, 60, 139));
		x11Colors.put(X11Col.SteelBlue, new RGB(70, 130, 180));
		x11Colors.put(X11Col.SteelBlue1, new RGB(99, 184, 255));
		x11Colors.put(X11Col.SteelBlue2, new RGB(92, 172, 238));
		x11Colors.put(X11Col.SteelBlue3, new RGB(79, 148, 205));
		x11Colors.put(X11Col.SteelBlue4, new RGB(54, 100, 139));
		x11Colors.put(X11Col.aquamarine, new RGB(127, 255, 212));
		x11Colors.put(X11Col.aquamarine1, new RGB(127, 255, 212));
		x11Colors.put(X11Col.aquamarine2, new RGB(118, 238, 198));
		x11Colors.put(X11Col.aquamarine3, new RGB(102, 205, 170));
		x11Colors.put(X11Col.aquamarine4, new RGB(69, 139, 116));
		x11Colors.put(X11Col.azure, new RGB(240, 255, 255));
		x11Colors.put(X11Col.azure1, new RGB(240, 255, 255));
		x11Colors.put(X11Col.azure2, new RGB(224, 238, 238));
		x11Colors.put(X11Col.azure3, new RGB(193, 205, 205));
		x11Colors.put(X11Col.azure4, new RGB(131, 139, 139));
		x11Colors.put(X11Col.blue, new RGB(0, 0, 255));
		x11Colors.put(X11Col.blue1, new RGB(0, 0, 255));
		x11Colors.put(X11Col.blue2, new RGB(0, 0, 238));
		x11Colors.put(X11Col.blue3, new RGB(0, 0, 205));
		x11Colors.put(X11Col.blue4, new RGB(0, 0, 139));
		x11Colors.put(X11Col.cyan, new RGB(0, 255, 255));
		x11Colors.put(X11Col.cyan1, new RGB(0, 255, 255));
		x11Colors.put(X11Col.cyan2, new RGB(0, 238, 238));
		x11Colors.put(X11Col.cyan3, new RGB(0, 205, 205));
		x11Colors.put(X11Col.cyan4, new RGB(0, 139, 139));
		x11Colors.put(X11Col.navy, new RGB(0, 0, 128));
		x11Colors.put(X11Col.turquoise, new RGB(64, 224, 208));
		x11Colors.put(X11Col.turquoise1, new RGB(0, 245, 255));
		x11Colors.put(X11Col.turquoise2, new RGB(0, 229, 238));
		x11Colors.put(X11Col.turquoise3, new RGB(0, 197, 205));
		x11Colors.put(X11Col.turquoise4, new RGB(0, 134, 139));
		x11Colors.put(X11Col.gray100, new RGB(255, 255, 255));
		x11Colors.put(X11Col.gray90, new RGB(229, 229, 229));
		x11Colors.put(X11Col.gray80, new RGB(204, 204, 204));
		x11Colors.put(X11Col.gray70, new RGB(179, 179, 179));
		x11Colors.put(X11Col.gray60, new RGB(153, 153, 153));
		x11Colors.put(X11Col.gray50, new RGB(127, 127, 127));
		x11Colors.put(X11Col.gray40, new RGB(102, 102, 102));
		x11Colors.put(X11Col.gray30, new RGB(77, 77, 77));
		x11Colors.put(X11Col.gray20, new RGB(51, 51, 51));
		x11Colors.put(X11Col.gray10, new RGB(26, 26, 26));
		x11Colors.put(X11Col.gray0, new RGB(0, 0, 0));
		x11Colors.put(X11Col.RosyBrown, new RGB(188, 143, 143));
		x11Colors.put(X11Col.RosyBrown1, new RGB(255, 193, 193));
		x11Colors.put(X11Col.RosyBrown2, new RGB(238, 180, 180));
		x11Colors.put(X11Col.RosyBrown3, new RGB(205, 155, 155));
		x11Colors.put(X11Col.RosyBrown4, new RGB(139, 105, 105));
		x11Colors.put(X11Col.SaddleBrown, new RGB(139, 69, 19));
		x11Colors.put(X11Col.SandyBrown, new RGB(244, 164, 96));
		x11Colors.put(X11Col.beige, new RGB(245, 245, 220));
		x11Colors.put(X11Col.brown, new RGB(165, 42, 42));
		x11Colors.put(X11Col.brown1, new RGB(255, 64, 64));
		x11Colors.put(X11Col.brown2, new RGB(238, 59, 59));
		x11Colors.put(X11Col.brown3, new RGB(205, 51, 51));
		x11Colors.put(X11Col.brown4, new RGB(139, 35, 35));
		x11Colors.put(X11Col.burlywood, new RGB(222, 184, 135));
		x11Colors.put(X11Col.burlywood1, new RGB(255, 211, 155));
		x11Colors.put(X11Col.burlywood2, new RGB(238, 197, 145));
		x11Colors.put(X11Col.burlywood3, new RGB(205, 170, 125));
		x11Colors.put(X11Col.burlywood4, new RGB(139, 115, 85));
		x11Colors.put(X11Col.chocolate, new RGB(210, 105, 30));
		x11Colors.put(X11Col.chocolate1, new RGB(255, 127, 36));
		x11Colors.put(X11Col.chocolate2, new RGB(238, 118, 33));
		x11Colors.put(X11Col.chocolate3, new RGB(205, 102, 29));
		x11Colors.put(X11Col.chocolate4, new RGB(139, 69, 19));
		x11Colors.put(X11Col.peru, new RGB(205, 133, 63));
		x11Colors.put(X11Col.tan, new RGB(210, 180, 140));
		x11Colors.put(X11Col.tan1, new RGB(255, 165, 79));
		x11Colors.put(X11Col.tan2, new RGB(238, 154, 73));
		x11Colors.put(X11Col.tan3, new RGB(205, 133, 63));
		x11Colors.put(X11Col.tan4, new RGB(139, 90, 43));
		x11Colors.put(X11Col.DarkGreen, new RGB(0, 100, 0));
		x11Colors.put(X11Col.DarkKhaki, new RGB(189, 183, 107));
		x11Colors.put(X11Col.DarkOliveGreen, new RGB(85, 107, 47));
		x11Colors.put(X11Col.DarkOliveGreen1, new RGB(202, 255, 112));
		x11Colors.put(X11Col.DarkOliveGreen2, new RGB(188, 238, 104));
		x11Colors.put(X11Col.DarkOliveGreen3, new RGB(162, 205, 90));
		x11Colors.put(X11Col.DarkOliveGreen4, new RGB(110, 139, 61));
		x11Colors.put(X11Col.DarkSeaGreen, new RGB(143, 188, 143));
		x11Colors.put(X11Col.DarkSeaGreen1, new RGB(193, 255, 193));
		x11Colors.put(X11Col.DarkSeaGreen2, new RGB(180, 238, 180));
		x11Colors.put(X11Col.DarkSeaGreen3, new RGB(155, 205, 155));
		x11Colors.put(X11Col.DarkSeaGreen4, new RGB(105, 139, 105));
		x11Colors.put(X11Col.ForestGreen, new RGB(34, 139, 34));
		x11Colors.put(X11Col.GreenYellow, new RGB(173, 255, 47));
		x11Colors.put(X11Col.LawnGreen, new RGB(124, 252, 0));
		x11Colors.put(X11Col.LightGreen, new RGB(144, 238, 144));
		x11Colors.put(X11Col.LightSeaGreen, new RGB(32, 178, 170));
		x11Colors.put(X11Col.LimeGreen, new RGB(50, 205, 50));
		x11Colors.put(X11Col.MediumSeaGreen, new RGB(60, 179, 113));
		x11Colors.put(X11Col.MediumSpringGreen, new RGB(0, 250, 154));
		x11Colors.put(X11Col.MintCream, new RGB(245, 255, 250));
		x11Colors.put(X11Col.OliveDrab, new RGB(107, 142, 35));
		x11Colors.put(X11Col.OliveDrab1, new RGB(192, 255, 62));
		x11Colors.put(X11Col.OliveDrab2, new RGB(179, 238, 58));
		x11Colors.put(X11Col.OliveDrab3, new RGB(154, 205, 50));
		x11Colors.put(X11Col.OliveDrab4, new RGB(105, 139, 34));
		x11Colors.put(X11Col.PaleGreen, new RGB(152, 251, 152));
		x11Colors.put(X11Col.PaleGreen1, new RGB(154, 255, 154));
		x11Colors.put(X11Col.PaleGreen2, new RGB(144, 238, 144));
		x11Colors.put(X11Col.PaleGreen3, new RGB(124, 205, 124));
		x11Colors.put(X11Col.PaleGreen4, new RGB(84, 139, 84));
		x11Colors.put(X11Col.SeaGreen, new RGB(46, 139, 87));
		x11Colors.put(X11Col.SeaGreen1, new RGB(84, 255, 159));
		x11Colors.put(X11Col.SeaGreen2, new RGB(78, 238, 148));
		x11Colors.put(X11Col.SeaGreen3, new RGB(67, 205, 128));
		x11Colors.put(X11Col.SeaGreen4, new RGB(46, 139, 87));
		x11Colors.put(X11Col.SpringGreen, new RGB(0, 255, 127));
		x11Colors.put(X11Col.SpringGreen1, new RGB(0, 255, 127));
		x11Colors.put(X11Col.SpringGreen2, new RGB(0, 238, 118));
		x11Colors.put(X11Col.SpringGreen3, new RGB(0, 205, 102));
		x11Colors.put(X11Col.SpringGreen4, new RGB(0, 139, 69));
		x11Colors.put(X11Col.YellowGreen, new RGB(154, 205, 50));
		x11Colors.put(X11Col.chartreuse, new RGB(127, 255, 0));
		x11Colors.put(X11Col.chartreuse1, new RGB(127, 255, 0));
		x11Colors.put(X11Col.chartreuse2, new RGB(118, 238, 0));
		x11Colors.put(X11Col.chartreuse3, new RGB(102, 205, 0));
		x11Colors.put(X11Col.chartreuse4, new RGB(69, 139, 0));
		x11Colors.put(X11Col.green, new RGB(0, 255, 0));
		x11Colors.put(X11Col.green1, new RGB(0, 255, 0));
		x11Colors.put(X11Col.green2, new RGB(0, 238, 0));
		x11Colors.put(X11Col.green3, new RGB(0, 205, 0));
		x11Colors.put(X11Col.green4, new RGB(0, 139, 0));
		x11Colors.put(X11Col.khaki, new RGB(240, 230, 140));
		x11Colors.put(X11Col.khaki1, new RGB(255, 246, 143));
		x11Colors.put(X11Col.khaki2, new RGB(238, 230, 133));
		x11Colors.put(X11Col.khaki3, new RGB(205, 198, 115));
		x11Colors.put(X11Col.khaki4, new RGB(139, 134, 78));
		x11Colors.put(X11Col.DarkOrange, new RGB(255, 140, 0));
		x11Colors.put(X11Col.DarkOrange1, new RGB(255, 127, 0));
		x11Colors.put(X11Col.DarkOrange2, new RGB(238, 118, 0));
		x11Colors.put(X11Col.DarkOrange3, new RGB(205, 102, 0));
		x11Colors.put(X11Col.DarkOrange4, new RGB(139, 69, 0));
		x11Colors.put(X11Col.DarkSalmon, new RGB(233, 150, 122));
		x11Colors.put(X11Col.LightCoral, new RGB(240, 128, 128));
		x11Colors.put(X11Col.LightSalmon, new RGB(255, 160, 122));
		x11Colors.put(X11Col.LightSalmon1, new RGB(255, 160, 122));
		x11Colors.put(X11Col.LightSalmon2, new RGB(238, 149, 114));
		x11Colors.put(X11Col.LightSalmon3, new RGB(205, 129, 98));
		x11Colors.put(X11Col.LightSalmon4, new RGB(139, 87, 66));
		x11Colors.put(X11Col.PeachPuff, new RGB(255, 218, 185));
		x11Colors.put(X11Col.PeachPuff1, new RGB(255, 218, 185));
		x11Colors.put(X11Col.PeachPuff2, new RGB(238, 203, 173));
		x11Colors.put(X11Col.PeachPuff3, new RGB(205, 175, 149));
		x11Colors.put(X11Col.PeachPuff4, new RGB(139, 119, 101));
		x11Colors.put(X11Col.bisque, new RGB(255, 228, 196));
		x11Colors.put(X11Col.bisque1, new RGB(255, 228, 196));
		x11Colors.put(X11Col.bisque2, new RGB(238, 213, 183));
		x11Colors.put(X11Col.bisque3, new RGB(205, 183, 158));
		x11Colors.put(X11Col.bisque4, new RGB(139, 125, 107));
		x11Colors.put(X11Col.coral, new RGB(255, 127, 80));
		x11Colors.put(X11Col.coral1, new RGB(255, 114, 86));
		x11Colors.put(X11Col.coral2, new RGB(238, 106, 80));
		x11Colors.put(X11Col.coral3, new RGB(205, 91, 69));
		x11Colors.put(X11Col.coral4, new RGB(139, 62, 47));
		x11Colors.put(X11Col.honeydew, new RGB(240, 255, 240));
		x11Colors.put(X11Col.honeydew1, new RGB(240, 255, 240));
		x11Colors.put(X11Col.honeydew2, new RGB(224, 238, 224));
		x11Colors.put(X11Col.honeydew3, new RGB(193, 205, 193));
		x11Colors.put(X11Col.honeydew4, new RGB(131, 139, 131));
		x11Colors.put(X11Col.orange, new RGB(255, 165, 0));
		x11Colors.put(X11Col.orange1, new RGB(255, 165, 0));
		x11Colors.put(X11Col.orange2, new RGB(238, 154, 0));
		x11Colors.put(X11Col.orange3, new RGB(205, 133, 0));
		x11Colors.put(X11Col.orange4, new RGB(139, 90, 0));
		x11Colors.put(X11Col.salmon, new RGB(250, 128, 114));
		x11Colors.put(X11Col.salmon1, new RGB(255, 140, 105));
		x11Colors.put(X11Col.salmon2, new RGB(238, 130, 98));
		x11Colors.put(X11Col.salmon3, new RGB(205, 112, 84));
		x11Colors.put(X11Col.salmon4, new RGB(139, 76, 57));
		x11Colors.put(X11Col.sienna, new RGB(160, 82, 45));
		x11Colors.put(X11Col.sienna1, new RGB(255, 130, 71));
		x11Colors.put(X11Col.sienna2, new RGB(238, 121, 66));
		x11Colors.put(X11Col.sienna3, new RGB(205, 104, 57));
		x11Colors.put(X11Col.sienna4, new RGB(139, 71, 38));
		x11Colors.put(X11Col.DarkRed, new RGB(139, 0, 0));
		x11Colors.put(X11Col.DeepPink, new RGB(255, 20, 147));
		x11Colors.put(X11Col.DeepPink1, new RGB(255, 20, 147));
		x11Colors.put(X11Col.DeepPink2, new RGB(238, 18, 137));
		x11Colors.put(X11Col.DeepPink3, new RGB(205, 16, 118));
		x11Colors.put(X11Col.DeepPink4, new RGB(139, 10, 80));
		x11Colors.put(X11Col.HotPink, new RGB(255, 105, 180));
		x11Colors.put(X11Col.HotPink1, new RGB(255, 110, 180));
		x11Colors.put(X11Col.HotPink2, new RGB(238, 106, 167));
		x11Colors.put(X11Col.HotPink3, new RGB(205, 96, 144));
		x11Colors.put(X11Col.HotPink4, new RGB(139, 58, 98));
		x11Colors.put(X11Col.IndianRed, new RGB(205, 92, 92));
		x11Colors.put(X11Col.IndianRed1, new RGB(255, 106, 106));
		x11Colors.put(X11Col.IndianRed2, new RGB(238, 99, 99));
		x11Colors.put(X11Col.IndianRed3, new RGB(205, 85, 85));
		x11Colors.put(X11Col.IndianRed4, new RGB(139, 58, 58));
		x11Colors.put(X11Col.LightPink, new RGB(255, 182, 193));
		x11Colors.put(X11Col.LightPink1, new RGB(255, 174, 185));
		x11Colors.put(X11Col.LightPink2, new RGB(238, 162, 173));
		x11Colors.put(X11Col.LightPink3, new RGB(205, 140, 149));
		x11Colors.put(X11Col.LightPink4, new RGB(139, 95, 101));
		x11Colors.put(X11Col.MediumVioletRed, new RGB(199, 21, 133));
		x11Colors.put(X11Col.MistyRose, new RGB(255, 228, 225));
		x11Colors.put(X11Col.MistyRose1, new RGB(255, 228, 225));
		x11Colors.put(X11Col.MistyRose2, new RGB(238, 213, 210));
		x11Colors.put(X11Col.MistyRose3, new RGB(205, 183, 181));
		x11Colors.put(X11Col.MistyRose4, new RGB(139, 125, 123));
		x11Colors.put(X11Col.OrangeRed, new RGB(255, 69, 0));
		x11Colors.put(X11Col.OrangeRed1, new RGB(255, 69, 0));
		x11Colors.put(X11Col.OrangeRed2, new RGB(238, 64, 0));
		x11Colors.put(X11Col.OrangeRed3, new RGB(205, 55, 0));
		x11Colors.put(X11Col.OrangeRed4, new RGB(139, 37, 0));
		x11Colors.put(X11Col.PaleVioletRed, new RGB(219, 112, 147));
		x11Colors.put(X11Col.PaleVioletRed1, new RGB(255, 130, 171));
		x11Colors.put(X11Col.PaleVioletRed2, new RGB(238, 121, 159));
		x11Colors.put(X11Col.PaleVioletRed3, new RGB(205, 104, 137));
		x11Colors.put(X11Col.PaleVioletRed4, new RGB(139, 71, 93));
		x11Colors.put(X11Col.VioletRed, new RGB(208, 32, 144));
		x11Colors.put(X11Col.VioletRed1, new RGB(255, 62, 150));
		x11Colors.put(X11Col.VioletRed2, new RGB(238, 58, 140));
		x11Colors.put(X11Col.VioletRed3, new RGB(205, 50, 120));
		x11Colors.put(X11Col.VioletRed4, new RGB(139, 34, 82));
		x11Colors.put(X11Col.firebrick, new RGB(178, 34, 34));
		x11Colors.put(X11Col.firebrick1, new RGB(255, 48, 48));
		x11Colors.put(X11Col.firebrick2, new RGB(238, 44, 44));
		x11Colors.put(X11Col.firebrick3, new RGB(205, 38, 38));
		x11Colors.put(X11Col.firebrick4, new RGB(139, 26, 26));
		x11Colors.put(X11Col.pink, new RGB(255, 192, 203));
		x11Colors.put(X11Col.pink1, new RGB(255, 181, 197));
		x11Colors.put(X11Col.pink2, new RGB(238, 169, 184));
		x11Colors.put(X11Col.pink3, new RGB(205, 145, 158));
		x11Colors.put(X11Col.pink4, new RGB(139, 99, 108));
		x11Colors.put(X11Col.red, new RGB(255, 0, 0));
		x11Colors.put(X11Col.red1, new RGB(255, 0, 0));
		x11Colors.put(X11Col.red2, new RGB(238, 0, 0));
		x11Colors.put(X11Col.red3, new RGB(205, 0, 0));
		x11Colors.put(X11Col.red4, new RGB(139, 0, 0));
		x11Colors.put(X11Col.tomato, new RGB(255, 99, 71));
		x11Colors.put(X11Col.tomato1, new RGB(255, 99, 71));
		x11Colors.put(X11Col.tomato2, new RGB(238, 92, 66));
		x11Colors.put(X11Col.tomato3, new RGB(205, 79, 57));
		x11Colors.put(X11Col.tomato4, new RGB(139, 54, 38));
		x11Colors.put(X11Col.BlanchedAlmond, new RGB(255, 235, 205));
		x11Colors.put(X11Col.DarkGoldenrod, new RGB(184, 134, 11));
		x11Colors.put(X11Col.DarkGoldenrod1, new RGB(255, 185, 15));
		x11Colors.put(X11Col.DarkGoldenrod2, new RGB(238, 173, 14));
		x11Colors.put(X11Col.DarkGoldenrod3, new RGB(205, 149, 12));
		x11Colors.put(X11Col.DarkGoldenrod4, new RGB(139, 101, 8));
		x11Colors.put(X11Col.LemonChiffon, new RGB(255, 250, 205));
		x11Colors.put(X11Col.LemonChiffon1, new RGB(255, 250, 205));
		x11Colors.put(X11Col.LemonChiffon2, new RGB(238, 233, 191));
		x11Colors.put(X11Col.LemonChiffon3, new RGB(205, 201, 165));
		x11Colors.put(X11Col.LemonChiffon4, new RGB(139, 137, 112));
		x11Colors.put(X11Col.LightGoldenrod, new RGB(238, 221, 130));
		x11Colors.put(X11Col.LightGoldenrod1, new RGB(255, 236, 139));
		x11Colors.put(X11Col.LightGoldenrod2, new RGB(238, 220, 130));
		x11Colors.put(X11Col.LightGoldenrod3, new RGB(205, 190, 112));
		x11Colors.put(X11Col.LightGoldenrod4, new RGB(139, 129, 76));
		x11Colors.put(X11Col.LightGoldenrodYellow, new RGB(250, 250, 210));
		x11Colors.put(X11Col.LightYellow, new RGB(255, 255, 224));
		x11Colors.put(X11Col.LightYellow1, new RGB(255, 255, 224));
		x11Colors.put(X11Col.LightYellow2, new RGB(238, 238, 209));
		x11Colors.put(X11Col.LightYellow3, new RGB(205, 205, 180));
		x11Colors.put(X11Col.LightYellow4, new RGB(139, 139, 122));
		x11Colors.put(X11Col.PaleGoldenrod, new RGB(238, 232, 170));
		x11Colors.put(X11Col.PapayaWhip, new RGB(255, 239, 213));
		x11Colors.put(X11Col.cornsilk, new RGB(255, 248, 220));
		x11Colors.put(X11Col.cornsilk1, new RGB(255, 248, 220));
		x11Colors.put(X11Col.cornsilk2, new RGB(238, 232, 205));
		x11Colors.put(X11Col.cornsilk3, new RGB(205, 200, 177));
		x11Colors.put(X11Col.cornsilk4, new RGB(139, 136, 120));
		x11Colors.put(X11Col.gold, new RGB(255, 215, 0));
		x11Colors.put(X11Col.gold1, new RGB(255, 215, 0));
		x11Colors.put(X11Col.gold2, new RGB(238, 201, 0));
		x11Colors.put(X11Col.gold3, new RGB(205, 173, 0));
		x11Colors.put(X11Col.gold4, new RGB(139, 117, 0));
		x11Colors.put(X11Col.goldenrod, new RGB(218, 165, 32));
		x11Colors.put(X11Col.goldenrod1, new RGB(255, 193, 37));
		x11Colors.put(X11Col.goldenrod2, new RGB(238, 180, 34));
		x11Colors.put(X11Col.goldenrod3, new RGB(205, 155, 29));
		x11Colors.put(X11Col.goldenrod4, new RGB(139, 105, 20));
		x11Colors.put(X11Col.moccasin, new RGB(255, 228, 181));
		x11Colors.put(X11Col.yellow, new RGB(255, 255, 0));
		x11Colors.put(X11Col.yellow1, new RGB(255, 255, 0));
		x11Colors.put(X11Col.yellow2, new RGB(238, 238, 0));
		x11Colors.put(X11Col.yellow3, new RGB(205, 205, 0));
		x11Colors.put(X11Col.yellow4, new RGB(139, 139, 0));

	}

	private final int red;
	private final int green;
	private final int blue;

	public RGB(int red, int green, int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public int getBlue() {
		return blue;
	}
	
	public int getGreen() {
		return green;
	}
	
	public int getRed() {
		return red;
	}

	static public RGB getRGB(X11Col color) {
		return x11Colors.get(color);
	}
}
