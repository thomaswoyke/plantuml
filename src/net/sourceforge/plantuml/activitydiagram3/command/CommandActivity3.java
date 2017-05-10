/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.activitydiagram3.command;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public class CommandActivity3 extends SingleLineCommand2<ActivityDiagram3> {

	public static final String ENDING_GROUP = "(;|\\\\\\\\|(?<![/|<>}\\]])(?:[/<}\\]])|(?<!\\</?\\w{1,5})(?<!\\<img[^>]{1,999})(?<!\\<\\$\\w{1,999})(?<!\\>)(?:\\>)|(?<!\\|.{1,999})(?:\\|))";

	public CommandActivity3() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf(":"), //
				new RegexLeaf("LABEL", "(.*)"), //
				new RegexLeaf("STYLE", ENDING_GROUP), //
				new RegexLeaf("$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, RegexResult arg) {

		final Url url;
		if (arg.get("URL", 0) == null) {
			url = null;
		} else {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			url = urlBuilder.getUrl(arg.get("URL", 0));
		}

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		final BoxStyle style = BoxStyle.fromChar(arg.get("STYLE", 0).charAt(0));
		diagram.addActivity(Display.getWithNewlines(arg.get("LABEL", 0)), style, url, colors);
		return CommandExecutionResult.ok();
	}

}