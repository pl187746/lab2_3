package edu.iis.mto.similarity;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import edu.iis.mto.search.SearchResult;
import edu.iis.mto.search.SequenceSearcher;

class SearcherDubler implements SequenceSearcher {
	
	public static class Result implements SearchResult {

		private boolean found;
		
		private int position;
		
		public Result(boolean found, int position) {
			super();
			this.found = found;
			this.position = position;
		}
		
		public boolean isFound() {
			return found;
		}

		public int getPosition() {
			return position;
		}
		
	}
	
	public static class SearchCall {
		public int key;
		public int[] seq;
		public Result result;
	}
	
	public List<SearchCall> searchCallHistory = new ArrayList<SearchCall>();

	public SearchResult search(int key, int[] seq) {
		SearchCall call = new SearchCall();
		call.key = key;
		call.seq = Arrays.copyOf(seq, seq.length);
		call.result = realSearch(key, seq);
		searchCallHistory.add(call);
		return call.result;
	}
	
	private Result realSearch(int key, int[] seq) {
		boolean f = false;
		int p = -1;
		for(int i = 0; i < seq.length; ++i) {
			if(key == seq[i]) {
				f = true;
				p = i;
			}
		}
		return new Result(f, p);
	}
	
}

public class SimilarityFinderTest {
	
	@Test
	public void identyczneSekwencje() {
		SearcherDubler searcherDubler = new SearcherDubler();
		SimilarityFinder similarityFinder = new SimilarityFinder(searcherDubler);
		int s1[] = { 1, 2, 3, 4 };
		int s2[] = { 1, 2, 3, 4 };
		double j = similarityFinder.calculateJackardSimilarity(s1, s2);
		assertThat(j, is(equalTo(1.0)));
	}
	
	@Test
	public void rozneSekwencje() {
		SearcherDubler searcherDubler = new SearcherDubler();
		SimilarityFinder similarityFinder = new SimilarityFinder(searcherDubler);
		int s1[] = { 1, 2, 3, 4 };
		int s2[] = { 9, 8, 7 };
		double j = similarityFinder.calculateJackardSimilarity(s1, s2);
		assertThat(j, is(equalTo(0.0)));
	}
	
}
