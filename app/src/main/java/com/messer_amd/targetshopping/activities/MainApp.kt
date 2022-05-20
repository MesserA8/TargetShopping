package com.messer_amd.targetshopping.activities

import android.app.Application
import com.messer_amd.targetshopping.db.MainDataBase

class MainApp : Application() {
    val database by lazy { MainDataBase.getDataBase(this ) }
}