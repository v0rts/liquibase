package liquibase.sqlgenerator;

import liquibase.database.Database;
import liquibase.database.InformixDatabase;
import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.statement.AddAutoIncrementStatement;

public class AddAutoIncrementGeneratorInformix extends AddAutoIncrementGenerator {
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    public boolean supports(AddAutoIncrementStatement statement, Database database) {
        return database instanceof InformixDatabase;
    }

    @Override
    public ValidationErrors validate(AddAutoIncrementStatement addAutoIncrementStatement, Database database) {
        ValidationErrors validationErrors = super.validate(addAutoIncrementStatement, database);

        validationErrors.checkRequiredField("columnDataType", addAutoIncrementStatement.getColumnDataType());

        return validationErrors;
    }

    public Sql[] generateSql(AddAutoIncrementStatement statement, Database database) {
        return new Sql[]{
                new UnparsedSql("ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) + " MODIFY " + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) + " " + database.getColumnType(statement.getColumnDataType(), true),
                        new Column()
                                .setTable(new Table(statement.getTableName()).setSchema(statement.getSchemaName()))
                                .setName(statement.getColumnName()))
        };
    }
}

