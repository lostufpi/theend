package br.com.ufpi.systematicmap.utils.service;

import java.util.LinkedList;
import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.com.ufpi.systematicmap.components.FilterArticles;

/**
 * @author Werney Ayala
 *
 */
@Named
@ApplicationScoped
public class TaskService {

	private Queue<FilterArticles> taskQueue;

	public TaskService() {
		this.taskQueue = new LinkedList<>();
	}

	public void addTask(FilterArticles value) {
		this.taskQueue.add(value);
	}

	public FilterArticles getFirstTask() {
		return this.taskQueue.poll();
	}

	/**
	 * @return the taskQueue
	 */
	public Queue<FilterArticles> getTaskQueue() {
		return taskQueue;
	}

	/**
	 * @param taskQueue
	 *            the taskQueue to set
	 */
	public void setTaskQueue(Queue<FilterArticles> taskQueue) {
		this.taskQueue = taskQueue;
	}
}