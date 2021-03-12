package com.lukeshay.discord.enums

enum class Issue(
    private val title: String,
    private val template: String,
) {
    BUG_REPORT("BUG", "bug_report.md"),
    FEATURE_REQUEST("FEATURE", "feature_request.md"),
    GUILD_TICKET("GUILD", "guild_ticket.md");

    override fun toString(): String {
        return "https://github.com/LukeShay/jeffery-krueger/issues/new?assignees=LukeShay&labels=${title.toLowerCase()}%2C+awaiting+triage&template=$template&title=%$title%5D+Your+title+here"
    }
}