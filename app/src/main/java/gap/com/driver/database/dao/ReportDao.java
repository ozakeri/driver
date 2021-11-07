package gap.com.driver.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import gap.com.driver.database.entity.Report;

@Dao
public interface ReportDao {

    @Query("SELECT * FROM report")
    List<Report> getAllReport();

    @Insert
    void insertReport(Report report);
}
