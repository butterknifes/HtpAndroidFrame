package com.example.htp.greendao.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.htp.greendao.entity.Note;
import com.example.htp.greendao.entity.User;

import com.example.htp.greendao.dao.NoteDao;
import com.example.htp.greendao.dao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteDaoConfig;
    private final DaoConfig userDaoConfig;

    private final NoteDao noteDao;
    private final UserDao userDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        noteDao = new NoteDao(noteDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Note.class, noteDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        noteDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
