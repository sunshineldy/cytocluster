package org.cytoscape.CytoCluster.internal.MyUtils;

import java.io.InputStream;
import java.net.URL;

public class Resources {

	public static URL getUrl(ImageName img) {
		System.out.println(Resources.class.getResource(img.toString()));
		return Resources.class.getResource(img.toString());
	}

	public static InputStream getInputStream(GO go) {
		System.out.println(Resources.class.getResource(go.toString()));
		return Resources.class.getResourceAsStream((go.toString()));
	}

	public static InputStream getInputStream(MIPS mips) {
		System.out.println(Resources.class.getResource(mips.toString()));
		return Resources.class.getResourceAsStream(mips.toString());
	}

	public static InputStream getInputStream(KNOWNCOMPLEXES kc) {
		System.out.println(Resources.class.getResource(kc.toString()));
		return Resources.class.getResourceAsStream(kc.toString());
	}

	// Enum type
	public static enum ImageName {
		ARROW_EXPANDED("/arrow_expanded.gif"), ARROW_COLLAPSED(
				"/arrow_collapsed.gif"), LOGO_SMALL("/arrow_collapsed.gif"), About_CSU(
				"/csu.png");

		private final String name;

		private ImageName(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}
	}

	// Enum GO
	public static enum GO {
		ANNOTATION("/GO/GO.annotation"), COMPONENT("/GO/GO.Component"), FUNCTION(
				"/GO/GO.Function"), PROCESS("/GO/GO.Process");

		private final String name;

		private GO(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}
	}

	// Enum MIPS
	public static enum MIPS {
		ANNOTATION("/MIPS/MIPS.annotation"), PROTEIN("/MIPS/MIPS.protein");

		private final String name;

		private MIPS(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}
	}

	// Enum MIPS
	public static enum KNOWNCOMPLEXES {
		Complex_MIPS("/KnownComplexes/Complex_MIPS(AT).txt");

		private final String name;

		private KNOWNCOMPLEXES(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}
	}
}
