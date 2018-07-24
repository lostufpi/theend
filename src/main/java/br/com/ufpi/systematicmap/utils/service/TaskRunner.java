package br.com.ufpi.systematicmap.utils.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.tasks.Task;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;
import br.com.ufpi.systematicmap.components.FilterArticles;
import br.com.ufpi.systematicmap.controller.MessagesController;
import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.enums.TipoMensagem;

@Scheduled(fixedRate = 60000, concurrent = false)
public class TaskRunner implements Task {

	@Inject	private TaskService service;
	@Inject	private EntityManagerFactory factory;

	private final Logger log = LoggerFactory.getLogger(getClass());

	public void execute() {

		FilterArticles task = service.getFirstTask();
		ArticleDao articleDao;
		
		if (task != null) {
			log.info("TaskRunner.execute()");

			EntityManager em = factory.createEntityManager();
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			articleDao = new ArticleDao(em);
			
			task.setArticleDao(articleDao);
			task.filter();
			
			MessagesController.addMessage(new Mensagem("mapstudy.filter.end.tittle", "mapstudy.filter.end.message", TipoMensagem.SUCESSO));
			MessagesController.changeRunner(false);

			log.info("TaskRunner.finalizado()");
			
			transaction.commit();
			em.close();
		}
	}
}
