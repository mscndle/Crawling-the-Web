#
#	webcrawler.py
#	
#	Simple webcrawler that finds links from a page and crawls up until a 1000 links
#	warning: super slow execution
#

from bs4 import BeautifulSoup
import urllib2

seed = "http://www.google.com"


# Returns the html contents of a page
def get_html(url):
	response = urllib2.urlopen(url)
	html = response.read()
	return html

# Returns the next url to be crawled
# If the url starts with a / or //, it then appends the domain automatically
# For now, cleaning up of the links is not handled
# This is going to get tricky, since adding the domain depends on the page we are currently crawling
def get_next_link(to_crawl):
	next_link = to_crawl.pop()
	return next_link

# Returns a list of links found from a string html input
def get_all_links(page):
	soup = BeautifulSoup(page)
	links = []
	links_formatted = []
	for link in soup.find_all('a'):
		href_link = link.get('href')
		links.append(href_link)

	return links		

# Main logic for the crawler
def crawler_logic(seed):
	crawled = []
	to_crawl = []
	graph = {}

	to_crawl.append(seed)	#start with this url and proceed to conquer the world
	next_link = seed
	count = 1000;

	while count > 0:
		next_link = to_crawl.pop()

		if next_link not in crawled:
			#crawl this link, add all of it's links to to_crawl
			page_html = get_html(next_link)

			if page_html:
				page_links = get_all_links(page_html)			
				to_crawl.append(page_links)	

			crawled.append(next_link)				

		count = count+1

	return crawled

if __name__ == "__main__":
	urls = crawler_logic(seed)
	print urls