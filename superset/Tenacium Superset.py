'''Tenacium Superset
   Tejas Aiyar'''

from superset import app, db
from superset.models.core import Database
from superset.connectors.sqla.models import SqlaTable
from superset.models.dashboard import Dashboard
from superset.models.slice import Slice

# Create a new database instance (replace this with your actual database configuration)
new_database = Database(database_name="example_db", sqlalchemy_uri="sqlite:///example.db")
db.session.add(new_database)
db.session.commit()

# Create a new table instance (replace this with your actual table details)
new_table = SqlaTable(
    table_name="example_table",
    database=new_database,
    schema="public",
    sql="SELECT * FROM example_table",
)
db.session.add(new_table)
db.session.commit()

# Create a new slice instance (replace this with your actual chart details)
new_slice = Slice(
    slice_name="Example Chart",
    datasource_type="table",
    datasource_id=new_table.id,
    viz_type="table",
    params="{}",
)
db.session.add(new_slice)
db.session.commit()

# Create a new dashboard instance
new_dashboard = Dashboard(
    dashboard_title="Example Dashboard",
    slug="example-dashboard",
)
new_dashboard.slices.append(new_slice)
db.session.add(new_dashboard)
db.session.commit()

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8088, debug=True)
