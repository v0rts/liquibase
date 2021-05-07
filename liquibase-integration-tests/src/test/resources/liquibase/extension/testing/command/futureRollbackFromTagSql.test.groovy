package liquibase.extension.testing.command

import liquibase.exception.CommandValidationException

CommandTests.define {
    command = ["futureRollbackFromTagSql"]
    signature = """
Short Description: Generates SQL to revert future changes up to the specified tag
Long Description: NOT SET
Required Args:
  changelogFile (String) The root changelog
  tag (String) Tag ID to rollback from
  url (String) The JDBC database connection URL
Optional Args:
  contexts (String) Changeset contexts to match
    Default: null
  labels (String) Changeset labels to match
    Default: null
  password (String) Password to use to connect to the database
    Default: null
  username (String) Username to use to connect to the database
    Default: null
"""
    run "Happy path", {
        arguments = [
                tag          : "version_2.0",
                changelogFile: "changelogs/hsqldb/complete/rollback.tag.changelog.xml",
        ]


        setup {
            runChangelog "changelogs/hsqldb/complete/rollback.tag.changelog.xml"
            rollback 5, "changelogs/hsqldb/complete/rollback.tag.changelog.xml"
        }

        expectedResults = [
                statusCode   : 0
        ]
    }

    run "Run without any arguments should throw an exception",  {
        arguments = [
                url: ""
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without a changeLogFile should throw an exception",  {
        arguments = [
                tag          : "version_2.0",
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without a tag should throw an exception",  {
        arguments = [
                changelogFile: "changelogs/hsqldb/complete/rollback.tag.changelog.xml",
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without a URL should throw an exception",  {
        arguments = [
                url          : "",
                changelogFile: "changelogs/hsqldb/complete/rollback.tag.changelog.xml",
                tag          : "version_2.0"
        ]
        expectedException = CommandValidationException.class
    }
}
