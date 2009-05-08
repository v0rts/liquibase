package liquibase.sqlgenerator;

import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.SQLiteDatabase;
import liquibase.database.structure.Index;
import liquibase.exception.JDBCException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.exception.UnsupportedChangeException;
import liquibase.sql.Sql;
import liquibase.statement.AddAutoIncrementStatement;
import liquibase.statement.SqlStatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SQLite does not support this ALTER TABLE operation until now.
 * For more information see: http://www.sqlite.org/omitted.html.
 * This is a small work around...
 */
public class AddAutoIncrementGeneratorSQLite extends AddAutoIncrementGenerator {

    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    public boolean supports(AddAutoIncrementStatement statement, Database database) {
        return database instanceof SQLiteDatabase;
    }

    public Sql[] generateSql(final AddAutoIncrementStatement statement, Database database) {
        List<Sql> statements = new ArrayList<Sql>();

        // define alter table logic
        SQLiteDatabase.AlterTableVisitor rename_alter_visitor = new SQLiteDatabase.AlterTableVisitor() {
            public ColumnConfig[] getColumnsToAdd() {
                return new ColumnConfig[0];
            }

            public boolean copyThisColumn(ColumnConfig column) {
                return true;
            }

            public boolean createThisColumn(ColumnConfig column) {
                if (column.getName().equals(statement.getColumnName())) {
                    column.setAutoIncrement(true);
                    column.setType("INTEGER");
                }
                return true;
            }

            public boolean createThisIndex(Index index) {
                return true;
            }
        };

        try {
            // alter table
            for (SqlStatement generatedStatement : SQLiteDatabase.getAlterTableStatements(
                    rename_alter_visitor,
                    database, statement.getSchemaName(), statement.getTableName())) {
                for (SqlGenerator generator : Arrays.asList(SqlGeneratorFactory.getInstance().getGenerator(generatedStatement, database))) {
                    statements.addAll(Arrays.asList(generator.generateSql(generatedStatement, database)));
                }
            }
        } catch (JDBCException e) {
            e.printStackTrace();
        } catch (UnsupportedChangeException e) {
            throw new UnexpectedLiquibaseException(e);
        }

        return statements.toArray(new Sql[statements.size()]);
    }
}