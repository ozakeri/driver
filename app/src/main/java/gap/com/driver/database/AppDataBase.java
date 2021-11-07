package gap.com.driver.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import gap.com.driver.database.dao.CommentDao;
import gap.com.driver.database.dao.DriverDao;
import gap.com.driver.database.dao.PersonTimeOffDao;
import gap.com.driver.database.dao.ReportDao;
import gap.com.driver.database.entity.CommentBean;
import gap.com.driver.database.entity.Driver;
import gap.com.driver.database.entity.PersonTimeOff;
import gap.com.driver.database.entity.Report;

@Database(entities = {PersonTimeOff.class, Driver.class, Report.class, CommentBean.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract PersonTimeOffDao personTimeOffDao();

    public abstract DriverDao driverDao();

    public abstract ReportDao reportDao();

    public abstract CommentDao commentDao();
}
