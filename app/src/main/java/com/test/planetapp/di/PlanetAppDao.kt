package com.test.planetapp.di

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.planetapp.model.Planet

@Dao
interface PlanetAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlanet(planet: Planet)


    @Query("SELECT * from planet_list")
    fun getAllPlanets(): List<Planet>?
}