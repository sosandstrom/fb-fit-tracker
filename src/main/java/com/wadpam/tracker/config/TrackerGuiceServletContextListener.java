package com.wadpam.tracker.config;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import com.wadpam.mardao.guice.MardaoGuiceModule;
import com.wadpam.mardao.oauth.web.OAuth2Filter;
import com.wadpam.tracker.dao.DParticipantDao;
import com.wadpam.tracker.dao.DParticipantDaoBean;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.dao.DRaceDaoBean;
import com.wadpam.tracker.dao.DTrackPointDao;
import com.wadpam.tracker.dao.DTrackPointDaoBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom
 * Date: 1/18/14 Time: 5:11 PM
 */
public class TrackerGuiceServletContextListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {

    return Guice.createInjector(
      new MardaoGuiceModule(),
      new JerseyServletModule() {

        private final void bindDaos() {
          bind(DRaceDao.class).to(DRaceDaoBean.class);
          bind(DParticipantDao.class).to(DParticipantDaoBean.class);
          bind(DTrackPointDao.class).to(DTrackPointDaoBean.class);
        }

        @Override
        protected void configureServlets() {
          bindDaos();

          filter("/*").through(PersistFilter.class);
          filter("/api/*").through(OAuth2Filter.class);

          Map<String, String> params = new HashMap<String, String>();
          params.put("com.sun.jersey.config.property.packages", "com.wadpam.tracker.api, " +
                  MardaoGuiceModule.JERSEY_PACKAGES);
          serve("/*").with(GuiceContainer.class, params);
        }
      }
    );
  }
}
