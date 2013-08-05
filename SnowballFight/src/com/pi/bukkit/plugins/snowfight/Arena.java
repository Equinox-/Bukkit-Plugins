package com.pi.bukkit.plugins.snowfight;

import org.bukkit.Location;

public class Arena {
    public static final String defaultVictoryMessage = "Your team, %MT, won!%n%MT%c %MP points  %OT%c %OP points";
    public static final String defaultDefeatedMessage = "Your team, %MT, lost %c'(%n%MT: %MP points  %OT%c %OP points";
    public static final String defaultTieMessage = "It was a tie between your team, %MT, and the opposing team, %OT!%n%MT%c %MP points  %OT%c %OP points";
    public static final String defaultRedTeamName = "The Red Team";
    public static final String defaultBlueTeamName = "The Blue Team";
    public static final String defaultWinningScoreMessage = "Your team, %MT, is leading with %MP points, against the opposing team, %OT, %OP points!";
    public static final String defaultLosingScoreMessage = "Your team, %MT, is losing with %MP points, against the opposing team, %OT, %OP points!";
    public static final String defaultTieScoreMessage = "Your team, %MT, is tied with %OT, with %MP points!";
    public static final String defaultAnonymousScoreMessage = "%MT is winning with %MP points against %OT, with %OP points!";

    public Cuboid waiting, redTeam, blueTeam, noTeamArea;
    public SnowballFight plugin;
    // %MP = my points, %OP = opposing points, %MT = My team name, %OT =
    // Opposing team name, \n, new line
    public String victoryMessage = defaultVictoryMessage;
    public String defeatedMessage = defaultDefeatedMessage;
    public String tieMessage = defaultTieMessage;
    public String redTeamName = defaultRedTeamName;
    public String blueTeamName = defaultBlueTeamName;
    public String tieScoreMessage = defaultTieScoreMessage;
    public String winningScoreMessage = defaultWinningScoreMessage;
    public String losingScoreMessage = defaultLosingScoreMessage;
    public String anonymousScoreMessage = defaultAnonymousScoreMessage;

    public Arena(SnowballFight plugin) {
	this.plugin = plugin;
    }

    public Cuboid getWaitingArea() {
	return waiting;
    }

    public Cuboid getRedTeam() {
	return redTeam;
    }

    public Cuboid getBlueTeam() {
	return blueTeam;
    }

    public boolean inPlayingArea(int x, int y, int z) {
	return redTeam.contains(x, y, z) || blueTeam.contains(x, y, z)
		|| noTeamArea.contains(x, y, z);
    }

    public boolean inPlayingArea(Location l) {
	return inPlayingArea(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public String getGameEndMessage(Team team, int myPoints, int opposingPoints) {
	String myTeamName = team == Team.RED ? redTeamName : blueTeamName;
	String opposingTeamName = team == Team.RED ? blueTeamName : redTeamName;
	String messageBase = myPoints > opposingPoints ? victoryMessage
		: myPoints < opposingPoints ? defeatedMessage : tieMessage;
	return replaceIgnoreCase(
		replaceIgnoreCase(
			replaceIgnoreCase(
				replaceIgnoreCase(
					replaceIgnoreCase(messageBase, "%mt",
						myTeamName), "%ot",
					opposingTeamName), "%mp",
				String.valueOf(myPoints)), "%op",
			String.valueOf(opposingPoints)), "%n", "\n");
    }

    public String getGameScoreMessage(Team team, int myPoints,
	    int opposingPoints) {
	String myTeamName = team == Team.RED ? redTeamName : blueTeamName;
	String opposingTeamName = team == Team.RED ? blueTeamName : redTeamName;
	String messageBase = myPoints > opposingPoints ? winningScoreMessage
		: myPoints < opposingPoints ? losingScoreMessage
			: tieScoreMessage;
	return replaceIgnoreCase(
		replaceIgnoreCase(
			replaceIgnoreCase(
				replaceIgnoreCase(
					replaceIgnoreCase(
						replaceIgnoreCase(messageBase,
							"%mt", myTeamName),
						"%ot", opposingTeamName),
					"%mp", String.valueOf(myPoints)),
				"%op", String.valueOf(opposingPoints)), "%n",
			"\n"), "%c", ":");
    }

    public String getAnonymousScoreMessage(int redPoints, int bluePoints) {
	String winningTeamName = redPoints > bluePoints ? redTeamName
		: blueTeamName;
	String losingTeamName = redPoints > bluePoints ? blueTeamName
		: redTeamName;
	return replaceIgnoreCase(
		replaceIgnoreCase(
			replaceIgnoreCase(
				replaceIgnoreCase(
					replaceIgnoreCase(
						anonymousScoreMessage, "%mt",
						winningTeamName), "%ot",
					losingTeamName), "%mp", String
					.valueOf(Math
						.max(redPoints, bluePoints))),
			"%op", String.valueOf(Math.min(redPoints, bluePoints))),
		"%n", "\n");
    }

    private static String replaceIgnoreCase(String haystack, String findB,
	    String replace) {
	String find = findB.toLowerCase();
	String lCase;
	while ((lCase = haystack.toLowerCase()).contains(find)) {
	    int pos = lCase.indexOf(find);
	    String p1 = haystack.substring(0, pos);
	    String p2 = haystack.substring(pos + find.length());
	    haystack = p1 + replace + p2;
	}
	return haystack;
    }
}
