import bentoml

model_runner = bentoml.models.get("ner-pipe").to_runner()

svc = bentoml.Service("ner-service", runners=[model_runner])


@svc.api(input=bentoml.io.JSON(), output=bentoml.io.JSON())
async def ner(input):
    results = await model_runner.async_run([input["text"]])
    return results[0]
