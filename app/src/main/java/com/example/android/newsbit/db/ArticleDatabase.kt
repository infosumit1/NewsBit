package com.example.android.newsbit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.newsbit.models.Article

/* this class defines the database configuration and
   serves as the app's main access point to the persisted data.
   part of Room setup(Dao, database, entities) */

@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class) //adds type converters with our database class
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object{
        //@Volatile means that writes to this field are immediately made visible to other threads.

        @Volatile
        private var instance: ArticleDatabase? = null

/* to ensure that only one instance of our database exist. basically synchoronize our database.
  I will refer room videos and articles to understand it. */

        private val LOCK = Any()

        /* invoke function is called when we create/initialize an instance of our database
           if instance is null then we start synchronized block with LOCK object so that everything
           inside this block can not be accessed by another thread at the same time  */

        //check out more about "also" in kotlin
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {

            instance ?: createDatabase(context).also { instance = it }
        }

        //read more about Room.databaseBuilder
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }

}