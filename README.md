CONCEPT

SafeRandom is a protocol for generating publicly verifiable random numbers. Our algorithm can be used to run secure, fair and transparent raffles and giveaways, in the sense that the Organizer cannot manipulate nor can deny the outcome.

*Note that this code has been implemented from scratch in 24 hours xtreme coding, due to the hackathon rules and it's not recommended for production use yet.

Awards:

3rd position in the 2017 National (Greek) Innovation Contest organised by National Bank of Greece.

4th position in the 1st Fintech CrowdHackathon, organised by National Bank of Greece and CrowdPolicy.

HOW IT WORKS

Unlike the conventional lotteries (using ping pong balls) or the electronic raffles (using an internal algorithm), our system utilizes public data that is very difficult to be “a-priori” predicted. I.e. Data sets with very high entropy.

This data set work as a “seed” to a well-defined public function, which guarantees to provide adequate output entropy. We currently use a variation of the SHA-256 algorithm, recently suggested by NIST (US National Institute of Standards and Technology) as a secure randomization extractor-function.

Some examples of public data sets that can be used include:

The next bitcoin block; no one can predict the next block in the chain (entropy ~2^65, depending on current difficulty).

The aggregated closing prices (to the 2nd decimal digit) of the stocks that comprise an index.

Weather conditions (temperature, wind, humidity) at a certain time in the major world capitals / cities.

Official flight landing times at biggest airports.

EXAMPLE:
Let’s assume we organize a raffle where the closing prices at June 29 of each of the NASDAQ-100 components are used as input seed. Then, on that day-time, anyone can run the algorithm by feeding it with the closing prices (publicly available on the web) and produce the raffle’s outcome. Everyone runs the same algorithm with the same input, so the output is the same for everyone; thus transparent, verified and undeniable.


VERIFIED LIST OF PARTICIPANTS

An extra feature of the protocol is the digital signature of the Terms & Conditions and the List Of Raffle Tickets, well before conducting the raffle. For that, we utilize the Bitcoin’s Blockchain, which guarantees the chronological sequence of transactions.
