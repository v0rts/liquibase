package liquibase.extension.testing.command

import liquibase.exception.CommandValidationException

CommandTests.define {
    command = ["unexpectedChangesets"]
    signature = """
Short Description: Generate a list of changesets that have been executed but are not in the current changelog
Long Description: NOT SET
Required Args:
  changelogFile (String) The root changelog
  url (String) The JDBC database connection URL
  verbose (String) Verbose flag
Optional Args:
  contexts (String) Changeset contexts to match
    Default: null
  password (String) Password to use to connect to the database
    Default: null
  username (String) Username to use to connect to the database
    Default: null
"""

    run "Happy path", {
        arguments = [
                verbose      : "true",
                changelogFile: "changelogs/hsqldb/complete/unexpected.tag.changelog.xml",
        ]

        setup {
            syncChangelog "changelogs/hsqldb/complete/rollback.tag.changelog.xml"
        }

        expectedResults = [
                statusCode   : 0
        ]
    }

    run "Run without a URL should throw an exception",  {
        arguments = [
                url          : "",
                verbose      : "true"
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without a changeLogFile should throw an exception",  {
        arguments = [
                verbose      : "true"
        ]
        expectedException = CommandValidationException.class
    }

    run "Run without any arguments should throw an exception",  {
        arguments = [
                url: "",
        ]
        expectedException = CommandValidationException.class
    }
}
