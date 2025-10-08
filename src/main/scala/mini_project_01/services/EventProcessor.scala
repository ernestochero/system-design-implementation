package mini_project_01.services

import cats.effect.IO
import fs2.Stream
import mini_project_01.domain.{Asset, AssetState}
import mini_project_01.repository.AssetRepository

object EventProcessor {
  def stream(repo: AssetRepository): Stream[IO, Unit] = {
    val events = List(
      ("asset1", "disney+", AssetState.Ready),
      ("asset2", "hulu", AssetState.Failed),
      ("asset3", "disney+", AssetState.Pending)
    )
    Stream.emits(events).covary[IO].evalMap { case (id, platform, state) =>
      val asset = Asset(id, platform, state)
      for {
        _ <- IO.println(s"[EventProcessor] Processing event for: $id")
        _ <- repo.upsert(asset)
      } yield ()
    }
  }
}
