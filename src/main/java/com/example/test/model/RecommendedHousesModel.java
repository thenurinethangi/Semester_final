package com.example.test.model;

import com.example.test.CrudUtility;
import com.example.test.db.DBConnection;
import com.example.test.dto.UnitDto;
import com.example.test.dto.tm.RequestTm;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;

public class RecommendedHousesModel {

    private final UnitModel unitModel = new UnitModel();

    public RecommendedHousesModel() throws SQLException, ClassNotFoundException {
    }

    public ObservableList<String> getAvailableRentHouses(RequestTm requestTm, String estimatedMonthlyBudgetForRent) throws SQLException, ClassNotFoundException {

        return unitModel.getAvailableRentHouses(requestTm,estimatedMonthlyBudgetForRent);
    }

    public ObservableList<String> getRecommendedRentHouses(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        return unitModel.getRecommendedRentHouses(requestTm);
    }

    public ObservableList<String> getAvailableSellHouses(RequestTm requestTm) throws SQLException, ClassNotFoundException {

       return unitModel.getAvailableSellHouses(requestTm);
    }

    public ObservableList<UnitDto> getAvailableRentHousesAsUnitDto(RequestTm requestTm, String estimatedMonthlyBudgetForRent) throws SQLException, ClassNotFoundException {

        return unitModel.getAvailableRentHousesAsUnitDto(requestTm,estimatedMonthlyBudgetForRent);
    }

    public ObservableList<UnitDto> getRecommendedRentHousesAsUnitDto(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        return unitModel.getRecommendedRentHousesAsUnitDto(requestTm);
    }

    public ObservableList<UnitDto> getAvailableSellHousesAsUnitDto(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        return unitModel.getAvailableSellHousesAsUnitDto(requestTm);
    }

    public String updateHouseId(UnitDto unitDto,RequestTm request) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();

        connection.setAutoCommit(false);

        try {
            String sqlStatementOne = "UPDATE buyAndRentRequest SET houseId = ? WHERE requestId = ?";
            boolean result = CrudUtility.execute(sqlStatementOne, unitDto.getHouseId(), request.getRequestId());

            if (result) {

                String sqlStatementTwo = "UPDATE house SET status = ? WHERE houseId = ?";
                boolean res = CrudUtility.execute(sqlStatementTwo,"Unavailable",unitDto.getHouseId());

                if(res){
                    connection.commit();
                    return "Successfully assign the house to the request";
                }
                else{
                    connection.rollback();
                    return "Something went wrong while updating the house status. Failed to assign the house to the request; please try again later.";
                }

            }
            else {
                connection.rollback();
                return "Something went wrong,Failed to assign the house to the request; please try again later";
            }
        }
        catch (Exception e){
            connection.rollback();
            e.printStackTrace();
        }
        finally {
            connection.setAutoCommit(true);
        }

        return "0";
    }

    public boolean makePreviousHouseAvailable(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        String sql = "UPDATE house SET status = ? WHERE houseId = ?";
        boolean result = CrudUtility.execute(sql,"Available",requestTm.getHouseId());

        return result;
    }

    public ObservableList<String> getRecommendedSellHouses(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        return unitModel.getRecommendedSellHouses(requestTm);
    }

    public ObservableList<UnitDto> getRecommendedSellHousesAsUnitDto(RequestTm requestTm) throws SQLException, ClassNotFoundException {

        return unitModel.getRecommendedSellHousesAsUnitDto(requestTm);
    }
}









