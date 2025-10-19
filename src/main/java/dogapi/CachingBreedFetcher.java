package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {

    private int callsMade = 0;
    private HashMap<String, List<String>> cache;
    private BreedFetcher fetcher;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        cache = new HashMap<String, List<String>>();
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException{
        List<String> lst = new ArrayList<String>();
        if (cache.containsKey(breed)) {
            lst = cache.get(breed);
        }
        else {
            try {
                lst = this.fetcher.getSubBreeds(breed);
                callsMade++;
                cache.put(breed, lst);
            }
            catch(BreedNotFoundException e) {
                callsMade++;
                throw new BreedNotFoundException(breed);
            }
        }
        // return statement included so that the starter code can compile and run.
        return lst;
    }

    public int getCallsMade() {
        return callsMade;
    }
}