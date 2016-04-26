package org.coenraets.cellar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class EmployeeDAO {

    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<Employee>();
        Connection c = null;
    	String sql = "SELECT * FROM employee ORDER BY name";
        try {
            c = ConnectionHelper.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }

    
    public List<Employee> findByName(String name) {
        List<Employee> list = new ArrayList<Employee>();
        Connection c = null;
    	String sql = "SELECT * FROM employee as e " +
			"WHERE UPPER(name) LIKE ? " +	
			"ORDER BY name";
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + name.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }
    
    public Employee findById(int id) {
    	String sql = "SELECT * FROM employee WHERE id = ?";
        Employee employee = null;
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employee = processRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return employee;
    }

    public Employee save(Employee employee)
	{
		return employee.getId() > 0 ? update(employee) : create(employee);
	}    
    
    public Employee create(Employee employee) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            ps = c.prepareStatement("INSERT INTO employee (name, grade, country, region, year, description) VALUES (?, ?, ?, ?, ?, ?)",
                new String[] { "ID" });
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getGrade());
            ps.setString(3, employee.getCountry());
            ps.setString(4, employee.getRegion());
            ps.setString(5, employee.getYear());
            ps.setString(6, employee.getDescription());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            // Update the id in the returned object. This is important as this value must be returned to the client.
            int id = rs.getInt(1);
            employee.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return employee;
    }

    public Employee update(Employee employee) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("UPDATE employee SET name=?, grade=?, country=?, region=?, year=?, description=? WHERE id=?");
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getGrade());
            ps.setString(3, employee.getCountry());
            ps.setString(4, employee.getRegion());
            ps.setString(5, employee.getYear());
            ps.setString(6, employee.getDescription());
            ps.setInt(7, employee.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return employee;
    }

    public boolean remove(int id) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM employee WHERE id=?");
            ps.setInt(1, id);
            int count = ps.executeUpdate();
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
    }

    protected Employee processRow(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setGrade(rs.getString("grade"));
        employee.setCountry(rs.getString("country"));
        employee.setRegion(rs.getString("region"));
        employee.setYear(rs.getString("year"));
        employee.setDescription(rs.getString("description"));
        return employee;
    }
    
}
