#
#	webcrawler.py
#	
#	Simple webcrawler that finds links from a page and crawls up until a 1000 links
#	warning: super slow execution
#

from bs4 import BeautifulSoup
import requests
import urllib2

seed = ""
urls_to_crawl = 0


# Returns the html contents of a page
def get_html(url):
	html = ""
	if type(url) == str:		
		pos1 = url.find("http://")
		pos2 = url.find("https://")

		if pos1 != -1 or pos2 != -1:
			response = requests.get(url)
			html = response.content

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

	while len(crawled) < urls_to_crawl:
		#print "entering while"

		if not to_crawl:	#in case the to_crawl is empty before the nubmer of urls to crawl
			break
		
		next_link = to_crawl.pop()
		if next_link not in crawled:
			#crawl this link, add all of it's links to to_crawl
			page_html = get_html(next_link)						
			page_links = get_all_links(page_html)			
			graph[next_link] = page_links

			for page in page_links:
				to_crawl.append(page)					

			crawled.append(next_link)				

	return graph

if __name__ == "__main__":
	seed = raw_input("Enter a seed URL (include http/s) : ")
	urls_to_crawl = raw_input("Enter the number of URLs to crawl : ")

	urls = crawler_logic(seed)
	print urls
	print len(urls)
	# print graph



