package com.test.planetapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.planetapp.db.converter.ResidentConverter
import com.test.planetapp.di.PlanetAppDao
import com.test.planetapp.model.Planet

@Database(entities = [Planet::class], version = 1)
@TypeConverters(ResidentConverter::class)
abstract class PlanetAppDB : RoomDatabase(){

    abstract fun planetAppDao(): PlanetAppDao

    companion object {
        val DB_NAME = "planet_app_db"

        @Volatile
        private var roomDatabaseInstance: PlanetAppDB? = null

        fun getDatabase(context: Context): PlanetAppDB?{
            if(roomDatabaseInstance == null){
                synchronized(PlanetAppDB::class.java) {
                    roomDatabaseInstance = Room.databaseBuilder(context, PlanetAppDB::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return roomDatabaseInstance
        }
    }
}